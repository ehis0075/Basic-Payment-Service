package com.payment.startup;


import com.payment.auth.model.UserSecurityDetails;
import com.payment.auth.service.UserSecurityDetailsService;
import com.payment.merchant.model.Merchant;
import com.payment.merchant.repository.MerchantRepository;
import com.payment.permission.enums.PermissionType;
import com.payment.permission.model.Permission;
import com.payment.permission.service.PermissionService;
import com.payment.role.model.MerchantRole;
import com.payment.role.repository.MerchantRoleRepository;
import com.payment.role.service.MerchantRoleService;
import com.payment.user.dto.CreateMerchantUserDTO;
import com.payment.user.model.MerchantUser;
import com.payment.user.repository.MerchantUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@AllArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

//    boolean alreadySetup = false;
    private final PermissionService permissionService;
    private final MerchantRepository adminMerchantRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSecurityDetailsService userSecurityDetailsService;
    private final MerchantUserRepository merchantUserRepository;
    private final MerchantRoleRepository merchantRoleRepository;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {


        //create all permissions
        permissionService.createPermissionsIfNecessary();

        //get all merchant permissions
        List<Permission> merchantPermissions = getAllPermissions();

        Merchant merchant = createMerchant();

        //create role super admin and assign all permissions and columns
        MerchantRole merchantUserRole = createRoleIfNotFound(merchantPermissions, merchant);

        CreateMerchantUserDTO createMerchantUserDTO = new CreateMerchantUserDTO();
        createMerchantUserDTO.setEmail("ehis@gmail.com");
        createMerchantUserDTO.setMerchantId(merchant.getMerchantId());
        createMerchantUserDTO.setRoleId(merchantUserRole.getId());
        createMerchantUserDTO.setPassword("ehis123$");

        UserSecurityDetails userSecurityDetails = userSecurityDetailsService.createUserSecurityDetails(createMerchantUserDTO);

        MerchantUser merchantUser = new MerchantUser();
        merchantUser.setMerchant(merchant);
        merchantUser.setRole(merchantUserRole);
        merchantUser.setUserSecurityDetails(userSecurityDetails);

        merchantUserRepository.save(merchantUser);

    }

    private Merchant createMerchant() {

        Merchant merchant = new Merchant();
        merchant.setName("Ehis Biz");
        merchant.setMerchantId(generateMerchantId("Ehis Biz"));

        return adminMerchantRepository.save(merchant);
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
        log.info("MID {}", merchantId);

        //check if merchantId is available
        return merchantId;
    }

    @Transactional
    public List<Permission> getAllPermissions() {
        return permissionService.getPermissionsByPermissionType(PermissionType.MERCHANT);
    }

    @Transactional
    public MerchantRole createRoleIfNotFound(List<Permission> permissions, Merchant merchant) {

        MerchantRole merchantRole = new MerchantRole();
        merchantRole.setPermissions(permissions);
        merchantRole.setName("Super Admin");
        merchantRole.setMerchant(merchant);

       return merchantRoleRepository.save(merchantRole);
    }
}