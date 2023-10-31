package com.payment.role.service;


import com.payment.permission.model.Permission;
import com.payment.role.dto.CreateUpdateRoleDTO;
import com.payment.role.dto.RoleDTO;
import com.payment.role.model.Role;

import java.util.List;

public interface RoleService {

    Role getRoleByName(String name);

    RoleDTO addRole(CreateUpdateRoleDTO roleDTO, String performedBy);

    Role addRole(Role role, String name, List<Permission> permissions);

    RoleDTO getAdminRoleDTO(Role adminRole);
}
