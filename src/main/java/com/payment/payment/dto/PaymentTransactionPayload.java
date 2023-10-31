package com.payment.payment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentTransactionPayload {

    private BigDecimal amount;

    private String merchantId;

}
