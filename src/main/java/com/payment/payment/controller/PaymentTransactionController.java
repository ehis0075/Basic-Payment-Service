package com.payment.payment.controller;


import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import com.payment.payment.dto.PaymentTransactionDTO;
import com.payment.payment.dto.PaymentTransactionPayload;
import com.payment.payment.model.PaymentTransaction;
import com.payment.payment.service.PaymentTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    private final GeneralService generalService;

    @PostMapping("/pay")
    public Response payment(@RequestBody PaymentTransactionPayload requestDTO) {

        PaymentTransactionDTO data = paymentTransactionService.savePaymentTransaction(requestDTO);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    @GetMapping("/{referenceNumber}")
    public Response getOneTransaction(@PathVariable String referenceNumber) {

        PaymentTransactionDTO data = paymentTransactionService.findPaymentTransactionByReferenceNumber(referenceNumber);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    @GetMapping("/get/{merchantId}")
    public Response getAllTransaction(@PathVariable String merchantId) {

        List<PaymentTransaction> data = paymentTransactionService.findPaymentTransactionListForOneMerchant(merchantId);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

}
