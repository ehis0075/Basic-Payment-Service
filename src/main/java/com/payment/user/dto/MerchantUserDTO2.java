package com.payment.user.dto;


import com.payment.role.dto.MerchantRoleDTO;
import lombok.Data;

@Data
public class MerchantUserDTO2 {
    private Long id;

    private String email;

    private MerchantRoleDTO role;

    private boolean isLoggedIn;


}
