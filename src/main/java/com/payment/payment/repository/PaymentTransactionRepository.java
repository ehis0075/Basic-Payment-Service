package com.payment.payment.repository;

import com.payment.payment.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    PaymentTransaction findByPaymentReferenceNumber(String paymentReferenceNumber);

    List<PaymentTransaction> findTop10ByPaymentStatusOrderByTransactionDateAsc(String paymentStatus);
    List<PaymentTransaction> findAllByMerchantId(String merchantId);
}
