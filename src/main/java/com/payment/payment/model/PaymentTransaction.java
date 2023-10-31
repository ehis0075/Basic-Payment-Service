package com.payment.payment.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)

    private String paymentReferenceNumber;

    private BigDecimal amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    private String paymentStatus;

    private String merchantId;
}
