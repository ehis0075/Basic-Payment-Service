package com.payment.payment.service.impl;

import com.payment.exception.GeneralException;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.merchant.repository.MerchantRepository;
import com.payment.payment.dto.PaymentTransactionDTO;
import com.payment.payment.dto.PaymentTransactionPayload;
import com.payment.payment.model.PaymentTransaction;
import com.payment.payment.repository.PaymentTransactionRepository;
import com.payment.payment.service.PaymentTransactionService;
import com.payment.util.GeneralUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
@AllArgsConstructor
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    private final MerchantRepository merchantRepository;

    @Override
    public PaymentTransactionDTO savePaymentTransaction(PaymentTransactionPayload payload) {
        log.info("Request to save transaction payment with payload {}", payload);

        if (Objects.isNull(payload.getMerchantId())) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "Merchant ID cannot be null or empty!");
        }

        // validate that merchant exist
        boolean isExist = merchantRepository.existsByMerchantId(payload.getMerchantId());

        if (!isExist) {
            throw new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88.responseCode, "Merchant Not Found");
        }

        Date transactionDate = new Date();

        String referenceNumber = payload.getMerchantId() + GeneralUtil.generateUniqueReferenceNumber(transactionDate);

        BigDecimal amount = GeneralUtil.getAmount(payload.getAmount());

        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setPaymentStatus("Pending");
        paymentTransaction.setPaymentReferenceNumber(referenceNumber);
        paymentTransaction.setTransactionDate(transactionDate);
        paymentTransaction.setAmount(amount);
        paymentTransaction.setMerchantId(payload.getMerchantId());

        PaymentTransaction savedPaymentTransaction = paymentTransactionRepository.save(paymentTransaction);

        // get dto
        return getPaymentTransactionDto(savedPaymentTransaction);
    }

    @Override
    public void pushTransaction() {
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findTop10ByPaymentStatusOrderByTransactionDateAsc("Pending");

        int size = paymentTransactionList.size();
        if (size > 0) {
            log.info("Pushing transactions {} from pending to successful", size);
            paymentTransactionList.forEach(this::pushTransaction);

        } else {
            log.info("Nothing to push");
        }

    }

    @Override
    public PaymentTransactionDTO findPaymentTransactionByReferenceNumber(String refNumber) {
        log.info("Getting Payment transaction with payment Ref Number {}", refNumber);

        if (Objects.isNull(refNumber)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "reference Number cannot be null or empty!");
        }

        PaymentTransaction paymentTransaction = paymentTransactionRepository.findByPaymentReferenceNumber(refNumber);

        if (Objects.isNull(paymentTransaction)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "Payment transaction not Found!");
        }

        return getPaymentTransactionDto(paymentTransaction);
    }

    @Override
    public List<PaymentTransaction> findPaymentTransactionListForOneMerchant(String merchantId) {
        log.info("Getting Payment transaction list with merchant Id {}", merchantId);

        if (Objects.isNull(merchantId)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "Merchant ID cannot be null or empty!");
        }

        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAllByMerchantId(merchantId);

        if (Objects.isNull(paymentTransactionList)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "No Payment transaction found for this merchant");
        }

        return paymentTransactionList;
    }

    private void pushTransaction(PaymentTransaction transaction) {

        transaction.setPaymentStatus("Completed");

        paymentTransactionRepository.save(transaction);
        log.info("Successfully changed Payment transaction status to successful");

    }

    private PaymentTransactionDTO getPaymentTransactionDto(PaymentTransaction paymentTransaction) {
        log.info("Converting payment transaction to payment transaction dto");

        PaymentTransactionDTO paymentTransactionDTO = new PaymentTransactionDTO();
        paymentTransactionDTO.setAmount(paymentTransaction.getAmount());
        paymentTransactionDTO.setMerchantId(paymentTransactionDTO.getMerchantId());
        paymentTransactionDTO.setPaymentReferenceNumber(paymentTransaction.getPaymentReferenceNumber());
        paymentTransactionDTO.setTransactionDate(paymentTransaction.getTransactionDate());
        paymentTransactionDTO.setMerchantId(paymentTransaction.getMerchantId());
        paymentTransactionDTO.setPaymentStatus(paymentTransaction.getPaymentStatus());

        return paymentTransactionDTO;

    }
}
