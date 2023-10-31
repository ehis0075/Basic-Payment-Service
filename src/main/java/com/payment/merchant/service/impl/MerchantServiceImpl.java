package com.payment.merchant.service.impl;


import com.payment.exception.GeneralException;
import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import com.payment.merchant.dto.AdminUserDTO;
import com.payment.merchant.dto.CreateUpdateUserDTO;
import com.payment.merchant.model.Merchant;
import com.payment.merchant.repository.MerchantRepository;
import com.payment.merchant.service.MerchantService;
import com.payment.role.dto.RoleDTO;
import com.payment.role.model.Role;
import com.payment.role.repository.RoleRepository;
import com.payment.role.service.RoleService;
import com.payment.security.JwtService;
import com.payment.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    private final RoleRepository adminRoleRepository;
    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final GeneralService generalService;
    private final AuthenticationManager authenticationManager;
    private final RoleService adminRoleService;
    private final MerchantRepository adminMerchantRepository;


    @Override
    public Response signIn(String username, String password) {
        log.info("Request to login with username {}", username);

        try {

            Merchant user = merchantRepository.findByUsername(username);

            Role userRole = user.getUserRole();

            //generate jwt token
            String token = jwtService.generateToken(username, userRole);

            Response response = new Response();
            response.setResponseCode(ResponseCodeAndMessage.SUCCESSFUL_0.responseCode);
            response.setResponseMessage(ResponseCodeAndMessage.SUCCESSFUL_0.responseMessage);
            response.setData(token);

            log.info("Successfully logged-in user {}", username);

            return response;

        } catch (AuthenticationException e) {
            log.info("Incorrect User credentials");
            throw new GeneralException(ResponseCodeAndMessage.AUTHENTICATION_FAILED_95);
        }
    }

    @Override
    public String getLoggedInUser() {
        log.info("Getting logged in user");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();

    }

    @Override
    public AdminUserDTO addUser(CreateUpdateUserDTO createUserDto) {
        log.info("creating a merchant user with payload {}", createUserDto);

        //validate first name, last name and phone number
        GeneralUtil.validateUsernameAndEmail(createUserDto.getUsername(), createUserDto.getEmail());

        // email to lower case
        String email = createUserDto.getEmail().toLowerCase();

        // check that email is not null or empty
        validateEmail(email);

        //get the role
        Role role = getRole(createUserDto.getRoleId());

        //create new user
        Merchant merchant = new Merchant();
        merchant.setUsername(createUserDto.getUsername());
        merchant.setEmail(email);
        merchant.setMerchantBusinessName(createUserDto.getBizName());
        merchant.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        merchant.setUserRole(role);
        merchant.setMerchantId(generateMerchantId(createUserDto.getUsername()));

        // save to db
        Merchant savedMerchant = saveAdminUser(merchant);

        // convert to dto
        return getUserDTO(savedMerchant);
    }

    @Override
    public AdminUserDTO getUserDTO(Merchant merchant) {
        log.info("Converting Admin User to Admin User DTO");

        AdminUserDTO adminUserDTO = new AdminUserDTO();

        generalService.createDTOFromModel(merchant, adminUserDTO);

        //get role dto
        RoleDTO roleDTO = adminRoleService.getAdminRoleDTO(merchant.getUserRole());
        adminUserDTO.setRole(roleDTO);

        return adminUserDTO;
    }

    @Override
    public Merchant get(String username) {
        log.info("Converting Admin User to Admin User DTO");

        return merchantRepository.findByUsername(username);
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

    static String generateRandomNumbers() {
        double a = Math.random();
        return Double.toString(a).split("\\.")[1];
    }


    private void validateEmail(String email) {

        if (adminMerchantRepository.existsByEmail(email)) {
            throw new GeneralException(ResponseCodeAndMessage.ALREADY_EXIST_86.responseCode, "Email already exist");
        }
    }

    private Role getRole(Long roleId) {
        if (Objects.isNull(roleId)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "Role id cannot be empty!");
        }

        return adminRoleRepository.findById(roleId).orElseThrow(() -> new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88.responseCode, "Role id cannot be empty"));
    }

    private Merchant saveAdminUser(Merchant merchant) {
        log.info("::: saving admin user to db :::");
        return adminMerchantRepository.save(merchant);
    }
}
