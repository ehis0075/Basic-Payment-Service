package com.payment.user.dto;


import com.payment.auth.dto.UserSecurityDetailsDTO;
import com.payment.merchant.dto.MerchantDTO;
import com.payment.role.dto.MerchantRoleDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
public class MerchantUserDTO {
    private Long id;

    private String email;

    private boolean enabled;

    private String userType;

    private MerchantRoleDTO role;

    private MerchantDTO merchant;

    private UserSecurityDetailsDTO userSecurityDetailsDTO;
}
