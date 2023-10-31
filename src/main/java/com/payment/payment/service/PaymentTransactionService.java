package com.payment.payment.service;

import com.payment.payment.dto.PaymentTransactionDTO;
import com.payment.payment.dto.PaymentTransactionPayload;
import com.payment.payment.model.PaymentTransaction;

import java.util.List;

public interface PaymentTransactionService {

    PaymentTransactionDTO savePaymentTransaction(PaymentTransactionPayload payload);

    void pushTransaction();

    PaymentTransactionDTO findPaymentTransactionByReferenceNumber(String refNumber);

    List<PaymentTransaction> findPaymentTransactionListForOneMerchant(String merchantId);
}
