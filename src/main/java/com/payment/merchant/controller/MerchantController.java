package com.payment.merchant.controller;

import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import com.payment.merchant.dto.CreateMerchantDTO;
import com.payment.merchant.dto.MerchantDTO;
import com.payment.merchant.service.MerchantService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/merchants")
public class MerchantController {

    private final GeneralService generalService;
    private final MerchantService merchantService;

    public MerchantController(GeneralService generalService, MerchantService merchantService) {
        this.generalService = generalService;
        this.merchantService = merchantService;
    }

    @PostMapping("/create")
    public Response createAccount(@RequestBody CreateMerchantDTO requestDTO) {

        MerchantDTO data = merchantService.createMerchant(requestDTO);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }
}
