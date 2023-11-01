package com.payment.permission.service.implementation;


import com.payment.general.service.GeneralService;
import com.payment.permission.dto.PermissionDTO;
import com.payment.permission.enums.PermissionName;
import com.payment.permission.enums.PermissionType;
import com.payment.permission.model.Permission;
import com.payment.permission.repository.PermissionRepository;
import com.payment.permission.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    private final GeneralService generalService;
    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(GeneralService generalService, PermissionRepository permissionRepository) {
        this.generalService = generalService;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission findByName(String name) {
        return permissionRepository.findByName(name);
    }

    @Override
    public PermissionDTO getPermissionDTO(Permission permission) {
        PermissionDTO permissionDTO = new PermissionDTO();
        generalService.createDTOFromModel(permission, permissionDTO);
        return permissionDTO;
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
    public void createPermissionsIfNecessary() {
        List<Permission> permissionList = new ArrayList<>();

        for (PermissionName permissionName : PermissionName.values()) {
            String name = permissionName.name();
            if (!existByName(name)) {
                Permission permission = getPermission(name, permissionName.permissionType);
                permissionList.add(permission);
            }
        }

        permissionRepository.saveAll(permissionList);
    }

    private Permission getPermission(String name, PermissionType permissionType) {
        log.info("Creating new permission {}", name);

        Permission permission = new Permission();
        permission.setName(name);
        permission.setPermissionType(permissionType);
        return permission;
    }

    private boolean existByName(String permissionName) {
        return permissionRepository.existsByName(permissionName);
    }

}
