package com.payment.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateMerchantUserDTO {

    private String merchantId;

    private String email;

    private Long roleId;

    private String password;

}
