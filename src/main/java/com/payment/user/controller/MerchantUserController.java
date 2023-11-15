package com.payment.user.controller;

import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import com.payment.user.dto.CreateMerchantUserDTO;
import com.payment.user.dto.MerchantUserDTO;
import com.payment.user.model.MerchantUser;
import com.payment.user.service.MerchantUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/merchantUsers")
public class MerchantUserController {

    private final MerchantUserService userService;
    private final GeneralService generalService;

    public MerchantUserController(MerchantUserService userService, GeneralService generalService) {
        this.userService = userService;
        this.generalService = generalService;
    }

    @PostMapping("/create")
    public Response createUser(@RequestBody CreateMerchantUserDTO requestDTO) {

        MerchantUserDTO data = userService.addUser(requestDTO);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    @PostMapping()
    public Response getAllMerchantUser() {

        List<MerchantUser> data = userService.getAll();
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

}
