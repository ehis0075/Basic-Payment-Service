package com.payment.merchant.dto;


import com.payment.role.dto.RoleDTO;
import lombok.Data;

@Data
public class AdminUserDTO {

    private Long id;

    private String username;

    private String email;

    private RoleDTO role;

    private String merchantId;

    private String merchantBusinessName;

}
