package com.payment.auth.service.implementation;


import com.payment.auth.model.UserSecurityDetails;
import com.payment.auth.repository.UserSecurityDetailsRepository;
import com.payment.auth.service.UserSecurityDetailsService;
import com.payment.role.service.MerchantRoleService;
import com.payment.user.dto.CreateMerchantUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Service
@Slf4j
public class UserSecurityDetailsServiceImpl implements UserSecurityDetailsService {
    private PasswordEncoder passwordEncoder;
    private MerchantRoleService merchantRoleService;
    private UserSecurityDetailsRepository userSecurityDetailsRepository;

    @Override
    public UserSecurityDetails createUserSecurityDetails(Object requestObject) {
        return createMerchantUserSecurityDetails((CreateMerchantUserDTO) requestObject);
    }

    public boolean validateEmail(String email) {
        log.info("Request to validate {}", email);
        return userSecurityDetailsRepository.existsByEmail(email);
    }

    @Override
    public UserSecurityDetails saveUserSecurityDetails(UserSecurityDetails userSecurityDetails) {
        return userSecurityDetailsRepository.save(userSecurityDetails);
    }

    private UserSecurityDetails createMerchantUserSecurityDetails(CreateMerchantUserDTO createMerchantUserDTO) {
        UserSecurityDetails userSecurityDetails = UserSecurityDetails.builder()
                .userType("MERCHANT_USER")
                .enabled(true)
                .locked(true)
                .authorities(retrieveMerchantPermissions(createMerchantUserDTO.getRoleId()))
                .password(passwordEncoder.encode(createMerchantUserDTO.getPassword()))
                .email(createMerchantUserDTO.getEmail())
                .roleName(retrieveMerchantUserRoleName(createMerchantUserDTO.getRoleId()))
                .roleId(createMerchantUserDTO.getRoleId())
                .isLoggedIn(false)
                .build();
        return userSecurityDetailsRepository.save(userSecurityDetails);
    }

    private Set<String> retrieveMerchantPermissions(Long roleId) {
        Set<String> permissions = new HashSet<>();
        merchantRoleService.getRoleById(roleId).getPermissions().forEach(permissionDTO -> permissions.add(permissionDTO.getName()));
        return permissions;
    }

    private String retrieveMerchantUserRoleName(Long roleId) {
        return merchantRoleService.getRoleById(roleId).getName();
    }

}
