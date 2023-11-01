package com.payment.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class UserAndMerchantIdResponse {
    private Long id;
    private String merchantId;
}
