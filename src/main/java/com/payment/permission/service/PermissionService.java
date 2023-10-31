package com.payment.permission.service;


import com.payment.permission.dto.PermissionDTO;
import com.payment.permission.dto.PermissionListDTO;
import com.payment.permission.dto.PermissionRequestDTO;
import com.payment.permission.enums.PermissionType;
import com.payment.permission.model.Permission;

import java.util.List;

public interface PermissionService {
    PermissionListDTO getAllPermission(PermissionRequestDTO requestDTO);

    Permission findByName(String name);

    PermissionDTO getPermissionDTO(Permission permission);

    List<Permission> getPermissionsByPermissionType(PermissionType permissionType);

    void createPermissionsIfNecessary();

}
