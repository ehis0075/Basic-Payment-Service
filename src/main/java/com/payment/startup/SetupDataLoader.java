package com.payment.startup;


import com.payment.merchant.constants.SuperAdminUserConstant;
import com.payment.merchant.model.Merchant;
import com.payment.merchant.repository.MerchantRepository;
import com.payment.permission.enums.PermissionType;
import com.payment.permission.model.Permission;
import com.payment.permission.service.PermissionService;
import com.payment.role.model.Role;
import com.payment.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${super.admin.emails}")
    private String superAdminEmails;
    boolean alreadySetup = false;
    private final RoleService adminRoleService;
    private final PermissionService permissionService;
    private final MerchantRepository adminMerchantRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(RoleService adminRoleService, PermissionService permissionService, MerchantRepository adminMerchantRepository, PasswordEncoder passwordEncoder) {
        this.adminRoleService = adminRoleService;
        this.permissionService = permissionService;
        this.adminMerchantRepository = adminMerchantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        //create all permissions
        permissionService.createPermissionsIfNecessary();

        //get all super-admin permissions
        List<Permission> adminPermissions = getAllPermissions();

        //create role super admin and assign all permissions and columns
        Role adminRole = createRoleIfNotFound(adminPermissions);

        List<Merchant> merchantList = new ArrayList<>();

        String[] adminEmails = superAdminEmails.split(",");
        for (int i = 0; i < adminEmails.length; i++) {

            // create super Admin
            Merchant superAdmin = createMerchant(adminRole, adminEmails[i]);
            if (Objects.nonNull(superAdmin)) {
                merchantList.add(superAdmin);
            }
        }

        // save super admin
        if (!merchantList.isEmpty()) {
            adminMerchantRepository.saveAll(merchantList);
        }

        alreadySetup = true;
    }

    private Merchant createMerchant(Role adminRole, String email) {

        if (!adminMerchantRepository.existsByEmail(email)) {
            Merchant merchant = new Merchant();
            merchant.setUsername(SuperAdminUserConstant.adminUserName);
            merchant.setEmail(email);
            merchant.setPassword(passwordEncoder.encode(SuperAdminUserConstant.password));
            merchant.setUserRole(adminRole);
            merchant.setMerchantId(generateMerchantId(SuperAdminUserConstant.bizName));
            return merchant;
        }

        return null;
    }

    static String generateRandomNumbers() {
        double a = Math.random();
        return Double.toString(a).split("\\.")[1];
    }

    static String generateMerchantId(String merchantName) {
        log.info("Generating merchant Id for {}", merchantName);

        merchantName = merchantName.replace(" ", "").toUpperCase(Locale.ROOT);

        String merchantId;
        if (merchantName.length() > 4) {
            merchantId = merchantName.substring(0, 4) + generateRandomNumbers().substring(0, 6);
        } else {
            merchantId = merchantName + generateRandomNumbers().substring(0, 10 - merchantName.length());
        }

        //check if merchantId is available
        return merchantId;
    }

    @Transactional
    public List<Permission> getAllPermissions() {
        return permissionService.getPermissionsByPermissionType(PermissionType.SUPER);
    }

    @Transactional
    public Role createRoleIfNotFound(List<Permission> permissions) {

        Role adminRole = adminRoleService.getRoleByName(SuperAdminUserConstant.adminUserRole);
        if (adminRole == null) {
            adminRole = new Role();
            adminRole = adminRoleService.addRole(adminRole, SuperAdminUserConstant.adminUserRole, permissions);
        }
        return adminRole;

    }
}