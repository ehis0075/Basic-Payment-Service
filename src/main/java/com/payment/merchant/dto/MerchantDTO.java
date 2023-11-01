package com.payment.merchant.dto;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MerchantDTO {
    private Long id;

    private String merchantId; //generated random 16 digits

    private String email;
}