package com.payment.role.service.implementation;


import com.payment.exception.GeneralException;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import com.payment.merchant.model.Merchant;
import com.payment.merchant.repository.MerchantRepository;
import com.payment.permission.dto.PermissionDTO;
import com.payment.permission.model.Permission;
import com.payment.permission.service.PermissionService;
import com.payment.role.dto.CreateUpdateMerchantRoleDTO;
import com.payment.role.dto.MerchantRoleDTO;
import com.payment.role.model.MerchantRole;
import com.payment.role.repository.MerchantRoleRepository;
import com.payment.role.service.MerchantRoleService;
import com.payment.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MerchantRoleServiceImpl implements MerchantRoleService {

    private final GeneralService generalService;
    private final MerchantRoleRepository merchantRoleRepository;
    private final PermissionService permissionService;
    private final MerchantRepository merchantRepository;

    public MerchantRoleServiceImpl(GeneralService generalService, MerchantRoleRepository merchantRoleRepository,
                                   PermissionService permissionService,
                                   MerchantRepository merchantRepository) {
        this.generalService = generalService;
        this.merchantRoleRepository = merchantRoleRepository;
        this.merchantRepository = merchantRepository;
        this.permissionService = permissionService;
    }

    @Override
    public MerchantRoleDTO addRole(CreateUpdateMerchantRoleDTO requestDTO) {
        log.info("Request to create a Role with payload = {}", requestDTO);

        // check that merchant id is not null or empty (valid)
        Merchant merchant = getByMerchantId(requestDTO.getMerchantId());

        //check if role name is valid
        if (GeneralUtil.stringIsNullOrEmpty(requestDTO.getName())) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "Role name cannot be empty or null");
        }

        //verify role with the merchant
        validateRoleName(requestDTO);

        //permission
        List<Permission> permissions = requestDTO.getPermissionNames().stream().map(permissionService::findByName).collect(Collectors.toList());

        MerchantRole role = new MerchantRole();

        // save role
        role = addRole(role, requestDTO.getName(), merchant, permissions);

        // get DTO
        return getRoleDTO(role);
    }

    @Override
    public MerchantRole addRole(MerchantRole role, String name, Merchant merchant, List<Permission> permissions) {
        role.setName(name);
        role.setMerchant(merchant);
        role.setPermissions(permissions);

        return merchantRoleRepository.save(role);
    }

    @Override
    public MerchantRoleDTO getRoleDTO(MerchantRole role) {

        MerchantRoleDTO merchantRoleDTO = new MerchantRoleDTO();
        generalService.createDTOFromModel(role, merchantRoleDTO);

        merchantRoleDTO.setMerchantId(role.getMerchant().getMerchantId());

        List<PermissionDTO> permissionDTOList = role.getPermissions().stream().map(permissionService::getPermissionDTO).collect(Collectors.toList());

        merchantRoleDTO.setPermissions(permissionDTOList);

        return merchantRoleDTO;
    }

    @Override
    public MerchantRoleDTO getRoleById(Long roleId) {
        log.info("Getting role by id {}", roleId);

        MerchantRole role = getRole(roleId);

        return getRoleDTO(role);
    }

    @Override
    public MerchantRole getMerchantRoleById(Long roleId) {
        return merchantRoleRepository.findById(roleId).orElseThrow(() -> new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88.responseCode, "Role does not exist"));
    }

    private void validateRoleName(CreateUpdateMerchantRoleDTO requestDTO) {
        boolean exists = merchantRoleRepository.existsByNameAndMerchant_MerchantId(requestDTO.getName(), requestDTO.getMerchantId());
        if (exists) {
            throw new GeneralException(ResponseCodeAndMessage.ALREADY_EXIST_86.responseCode, "Role " + requestDTO.getName() + " exist for this merchant");
        }
    }

    private Merchant getByMerchantId(String merchantId) {
        if (GeneralUtil.stringIsNullOrEmpty(merchantId)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "Merchant id cannot be empty or null");
        }

        Merchant merchant = merchantRepository.findByMerchantId(merchantId);
        if (Objects.isNull(merchant)) {
            throw new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88.responseCode, "Merchant not found");
        }
        return merchant;
    }

    private MerchantRole getRole(Long roleId) {
        return merchantRoleRepository.findById(roleId).orElseThrow(() -> new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88));
    }

}
