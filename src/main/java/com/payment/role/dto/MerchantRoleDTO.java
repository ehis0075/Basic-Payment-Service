package com.payment.role.dto;

import com.payment.permission.dto.PermissionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
public class MerchantRoleDTO {
    private Long id;

    private String name;

    private String merchantId;

    private List<PermissionDTO> permissions;
}