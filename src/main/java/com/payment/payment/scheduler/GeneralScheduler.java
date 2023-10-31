package com.payment.payment.scheduler;

import com.payment.payment.service.PaymentTransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableScheduling
@AllArgsConstructor
public class GeneralScheduler {

    private final PaymentTransactionService paymentTransactionService;

    @Scheduled(fixedDelayString = "5000")
    private void pushPendingTransaction() {
        log.info("--- Starting scheduler service ---");
        paymentTransactionService.pushTransaction();
    }
}
