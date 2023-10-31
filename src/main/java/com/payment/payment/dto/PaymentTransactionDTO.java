package com.payment.payment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentTransactionDTO {

    private String paymentReferenceNumber;

    private BigDecimal amount;

    private Date transactionDate;

    private String merchantId;
}
