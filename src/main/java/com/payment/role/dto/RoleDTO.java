package com.payment.role.dto;


import com.payment.permission.dto.PermissionDTO;
import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private Long id;

    private String name;

    private List<PermissionDTO> permissions;

}