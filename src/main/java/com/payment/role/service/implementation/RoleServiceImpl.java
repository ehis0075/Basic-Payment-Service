package com.payment.role.service.implementation;


import com.payment.exception.GeneralException;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import com.payment.merchant.repository.MerchantRepository;
import com.payment.permission.dto.PermissionDTO;
import com.payment.permission.model.Permission;
import com.payment.permission.service.PermissionService;
import com.payment.role.dto.CreateUpdateRoleDTO;
import com.payment.role.dto.RoleDTO;
import com.payment.role.dto.RoleListDTO;
import com.payment.role.model.Role;
import com.payment.role.repository.RoleRepository;
import com.payment.role.service.RoleService;
import com.payment.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final GeneralService generalService;
    private final RoleRepository roleRepository;
    private final MerchantRepository adminMerchantRepository;
    private final PermissionService permissionService;

    public RoleServiceImpl(GeneralService generalService, RoleRepository roleRepository, MerchantRepository adminMerchantRepository, PermissionService permissionService) {
        this.generalService = generalService;
        this.roleRepository = roleRepository;
        this.adminMerchantRepository = adminMerchantRepository;
        this.permissionService = permissionService;
    }

    @Override
    public Role getRoleByName(String name) {
        log.info("Getting Admin Role using name => {}", name);

        return roleRepository.findByName(name);
    }

    @Override
    public RoleDTO addRole(CreateUpdateRoleDTO requestDTO, String performedBy) {
        log.info("Request to create a Role with payload {} by user {}", requestDTO, performedBy);

        //check if role name is valid
        if (GeneralUtil.stringIsNullOrEmpty(requestDTO.getName())) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "Role name cannot be empty or null");
        }

        //verify role name
        validateRoleName(requestDTO.getName());

        //permission
        List<Permission> permissions = requestDTO.getPermissionNames().stream().map(permissionService::findByName).collect(Collectors.toList());


        Role role = new Role();

        // save role
        role = addRole(role, requestDTO.getName(), permissions);

        // get DTO
        return getAdminRoleDTO(role);
    }

    @Override
    public Role addRole(Role role, String name, List<Permission> permissions) {
        role.setName(name);
        role.setPermissions(permissions);

        return roleRepository.save(role);
    }

    @Override
    public RoleDTO getAdminRoleDTO(Role adminRole) {

        RoleDTO adminRoleDTO = new RoleDTO();
        generalService.createDTOFromModel(adminRole, adminRoleDTO);

        List<PermissionDTO> permissionDTOList = adminRole.getPermissions().stream().map(permissionService::getPermissionDTO).collect(Collectors.toList());

        adminRoleDTO.setPermissions(permissionDTOList);

        return adminRoleDTO;
    }

    public boolean arePermissionNamesValid(List<String> permissionNames) {
        return permissionNames != null && !permissionNames.isEmpty();
    }


    private Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88));
    }

    private void validateRoleName(String name) {
        boolean exists = roleRepository.existsByName(name);
        if (exists) {
            throw new GeneralException(ResponseCodeAndMessage.ALREADY_EXIST_86.responseCode, "Role " + name + " already exist");
        }
    }

    private RoleListDTO getAdminRoleListDTO(Page<Role> adminRolePage) {
        RoleListDTO listDTO = new RoleListDTO();

        List<Role> roleList = adminRolePage.getContent();
        if (adminRolePage.getContent().size() > 0) {
            listDTO.setHasNextRecord(adminRolePage.hasNext());
            listDTO.setTotalCount((int) adminRolePage.getTotalElements());
        }

        List<RoleDTO> roleDTOS = convertToAdminRoleDTOList(roleList);
        listDTO.setRoleDTOList(roleDTOS);

        return listDTO;
    }

    private List<RoleDTO> convertToAdminRoleDTOList(List<Role> roleList) {
        log.info("Converting Role List to Role DTO List");

        return roleList.stream().map(this::getAdminRoleDTO).collect(Collectors.toList());
    }
}
