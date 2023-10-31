package com.payment.payment.service.impl;

import com.payment.exception.GeneralException;
import com.payment.merchant.repository.MerchantRepository;
import com.payment.payment.dto.PaymentTransactionDTO;
import com.payment.payment.dto.PaymentTransactionPayload;
import com.payment.payment.model.PaymentTransaction;
import com.payment.payment.repository.PaymentTransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentTransactionServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @InjectMocks
    private PaymentTransactionServiceImpl paymentTransactionService;

    private PaymentTransactionPayload payload;

    @Before
    public void setUp() {
        payload = new PaymentTransactionPayload();
        payload.setAmount(BigDecimal.valueOf(100));
        payload.setMerchantId("merchant-123");
    }


    @Test
    public void shouldThrowExceptionWhenMerchantIdIsNull() {

        // Arrange
        PaymentTransactionPayload payload = new PaymentTransactionPayload();

        // Act
        assertThrows(GeneralException.class, () -> paymentTransactionService.savePaymentTransaction(payload));
    }

    @Test
    public void shouldThrowExceptionWhenMerchantDoesNotExist() {

        // Mocking the behavior of the merchantRepository
        PaymentTransactionPayload payload = new PaymentTransactionPayload();
        payload.setMerchantId("1234567890");
        when(merchantRepository.existsByMerchantId(payload.getMerchantId())).thenReturn(false);

        // Act
        assertThrows(GeneralException.class, () -> paymentTransactionService.savePaymentTransaction(payload));

    }

    @Test(expected = GeneralException.class)
    public void testSavePaymentTransaction_IncompleteParameters() {
        // Creating a payload with a null merchantId
        PaymentTransactionPayload incompletePayload = new PaymentTransactionPayload();
        incompletePayload.setAmount(BigDecimal.valueOf(100));

        // Calling the actual method
        paymentTransactionService.savePaymentTransaction(incompletePayload);
    }

    @Test(expected = GeneralException.class)
    public void testSavePaymentTransaction_MerchantNotFound() {
        // Mocking the behavior of the merchantRepository
        when(merchantRepository.existsByMerchantId(payload.getMerchantId())).thenReturn(false);

        // Calling the actual method
        paymentTransactionService.savePaymentTransaction(payload);
    }


    @Test
    public void testSavePaymentTransaction_Success() {
        // Mocking the behavior of the merchantRepository
        when(merchantRepository.existsByMerchantId(payload.getMerchantId())).thenReturn(true);

        // Mocking the behavior of the paymentTransactionRepository
        PaymentTransaction savedPaymentTransaction = new PaymentTransaction();
        savedPaymentTransaction.setId(1L);
        savedPaymentTransaction.setMerchantId(payload.getMerchantId());
        when(paymentTransactionRepository.save(any(PaymentTransaction.class))).thenReturn(savedPaymentTransaction);

        // Calling the actual method
        PaymentTransactionDTO result = paymentTransactionService.savePaymentTransaction(payload);

        // Assertions
        assertEquals(payload.getMerchantId(), result.getMerchantId());
    }
}