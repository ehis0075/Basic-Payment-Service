package com.payment.user.service.implementation;


import com.payment.auth.dto.UserAndMerchantIdResponse;
import com.payment.auth.dto.UserSecurityDetailsDTO;
import com.payment.auth.model.UserSecurityDetails;
import com.payment.auth.service.UserSecurityDetailsService;
import com.payment.exception.GeneralException;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import com.payment.merchant.dto.CreateMerchantDTO;
import com.payment.merchant.dto.MerchantDTO;
import com.payment.merchant.model.Merchant;
import com.payment.merchant.repository.MerchantRepository;
import com.payment.permission.enums.PermissionType;
import com.payment.permission.model.Permission;
import com.payment.permission.repository.PermissionRepository;
import com.payment.role.dto.MerchantRoleDTO;
import com.payment.role.model.MerchantRole;
import com.payment.role.repository.MerchantRoleRepository;
import com.payment.role.service.MerchantRoleService;
import com.payment.user.dto.CreateMerchantUserDTO;
import com.payment.user.dto.MerchantUserDTO;
import com.payment.user.model.MerchantUser;
import com.payment.user.repository.MerchantUserRepository;
import com.payment.user.service.MerchantUserService;
import com.payment.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class MerchantUserServiceImpl implements MerchantUserService {

    private final MerchantRoleService roleService;
    private final MerchantRoleRepository roleRepository;
    private final GeneralService generalService;
    private final MerchantUserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSecurityDetailsService userSecurityDetailsService;

    public MerchantUserServiceImpl(MerchantRoleService roleService, MerchantRoleRepository roleRepository,
                                   GeneralService generalService, MerchantUserRepository userRepository,
                                   MerchantRepository merchantRepository, PermissionRepository permissionRepository,
                                   PasswordEncoder passwordEncoder1, UserSecurityDetailsService userSecurityDetailsService) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.generalService = generalService;
        this.userRepository = userRepository;
        this.merchantRepository = merchantRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder1;
        this.userSecurityDetailsService = userSecurityDetailsService;
    }

    @Override
    public MerchantUserDTO addUser(CreateMerchantUserDTO createUserDto) {
        log.info("Request to create a Merchant user with payload = {}", createUserDto);

        // check that merchant id is not null
        Merchant merchant = getMerchant(createUserDto.getMerchantId());

        // check that email is not null or empty
        validateEmail(createUserDto.getEmail());

        //validate that email does not exist
        boolean isExist = userSecurityDetailsService.validateEmail(createUserDto.getEmail());
        if (isExist) {
            throw new GeneralException(ResponseCodeAndMessage.ALREADY_EXIST_86.responseCode, "Email already Exist!");
        }

        UserSecurityDetails userSecurityDetails = userSecurityDetailsService.createUserSecurityDetails(createUserDto);

        //create new user
        MerchantUser user = new MerchantUser();
        user.setMerchant(merchant);
        user.setUserSecurityDetails(userSecurityDetails);

        // save to db
        MerchantUser savedUser = userRepository.save(user);

        // convert to dto
        return getUserDTO(savedUser);
    }

    @Override
    public List<MerchantUser> getAll() {

        return userRepository.findAll();
    }

    @Override
    public void addFirstMerchantUser(CreateMerchantDTO merchantDTO, Merchant merchant) {
        log.info("Request to create first merchant User with payload {} {}", merchantDTO, merchant);

        //get merchant permissions
        List<Permission> permissions = getPermissionsByPermissionType(PermissionType.MERCHANT);

        //create admin role for merchant
        MerchantRole adminROle = new MerchantRole();
        adminROle = roleService.addRole(adminROle, "ADMIN", merchant, permissions);

        UserSecurityDetails userSecurityDetails = UserSecurityDetails
                .builder()
                .enabled(true)
                .email(merchantDTO.getEmail())
                .userType("MERCHANT_USER")
                .roleId(adminROle.getId())
                .password(passwordEncoder.encode(merchantDTO.getPassword()))
                .roleName(adminROle.getName())
                .authorities(getPermissions(permissions))
                .enabled(true)
                .locked(true)
                .build();

        userSecurityDetails = userSecurityDetailsService.saveUserSecurityDetails(userSecurityDetails);

        MerchantUser user = MerchantUser.builder()
                .merchant(merchant)
                .userSecurityDetails(userSecurityDetails)
                .build();

        saveUser(user);
    }

    Set<String> getPermissions(List<Permission> permissions) {
        Set<String> permission = new HashSet<>();
        permissions.forEach(permission1 -> permission.add(permission1.getName()));
        return permission;
    }

    @Override
    public List<Permission> getPermissionsByPermissionType(PermissionType permissionType) {
        if (permissionType.equals(PermissionType.SUPER)) {
            return permissionRepository.findBySuperAdminTrue();
        } else {
            return permissionRepository.findByMerchantAdminTrue();
        }
    }

    @Override
    public void validateThatUserDoesNotExist(String email) {
        log.info("Validating if user email {} already exist", email);

        validateEmail(email);
    }


    @Override
    public MerchantUserDTO getUserDTO(MerchantUser user) {
        log.info("merchant user {}", user);

        MerchantUserDTO userDTO = new MerchantUserDTO();
        generalService.createDTOFromModel(user, userDTO);
        userDTO.setEmail(user.getUserSecurityDetails().getEmail());
        userDTO.setUserType(user.getUserSecurityDetails().getUserType());

        UserSecurityDetails userSecurityDetails = user.getUserSecurityDetails();
        UserSecurityDetailsDTO userSecurityDetailsDTO = new UserSecurityDetailsDTO();

        generalService.createDTOFromModel(userSecurityDetails, userSecurityDetailsDTO);

        //ger merchant dto
        MerchantDTO merchantDTO = Merchant.getMerchantDTO(user.getMerchant());
        userDTO.setMerchant(merchantDTO);

        Long roleId = user.getUserSecurityDetails().getRoleId();
        log.info("role id {}", roleId);
        MerchantRole merchantRole = roleService.getMerchantRoleById(roleId);

        //get role dto
        MerchantRoleDTO roleDTO = roleService.getRoleDTO(merchantRole);

        // set the role
        userDTO.setRole(roleDTO);

        userDTO.setUserSecurityDetailsDTO(userSecurityDetailsDTO);

        userDTO.setEnabled(userSecurityDetailsDTO.isEnabled());

        return userDTO;
    }

    private void validateEmail(String email) {
        if (GeneralUtil.invalidEmail(email)) {
            log.info("User email {} is invalid", email);
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "User Email " + email + " is invalid!");
        }

        if (userRepository.existsByUserSecurityDetails_Email(email)) {
            throw new GeneralException(ResponseCodeAndMessage.ALREADY_EXIST_86.responseCode, "Email already exist");
        }
    }

    private MerchantRole getRole(Long roleId, String merchantId) {
        if (Objects.isNull(roleId)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "Role id cannot be empty!");
        }

        MerchantRole role = roleRepository.findByIdAndMerchant_MerchantId(roleId, merchantId);
        if (Objects.isNull(role)) {
            throw new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88.responseCode, "Role id cannot be empty");
        }

        return role;
    }

    private Merchant getMerchant(String merchantId) {

        if (GeneralUtil.stringIsNullOrEmpty(merchantId)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "Merchant id cannot be empty!");
        }

        Merchant merchant = merchantRepository.findByMerchantId(merchantId);
        if (Objects.isNull(merchant)) {
            throw new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88.responseCode, "Merchant Not Found");
        }

        return merchant;
    }

    @Override
    public MerchantUser saveUser(MerchantUser user) {
        return userRepository.save(user);
    }

    @Override
    public UserAndMerchantIdResponse getMerchantUserIdAndMerchantIdByMerchantEmail(String email) {
        MerchantUser merchantUser = userRepository.findByUserSecurityDetails_Email(email).orElseThrow(() ->
                new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88.responseCode, "Merchant Not Found"));
        return new UserAndMerchantIdResponse(merchantUser.getId(), merchantUser.getMerchant().getMerchantId());
    }

}