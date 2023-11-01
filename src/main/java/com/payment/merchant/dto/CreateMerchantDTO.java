package com.payment.merchant.dto;

import lombok.Data;

@Data
public class CreateMerchantDTO {

    private String email;

    private String password;

    private String businessName;
}
