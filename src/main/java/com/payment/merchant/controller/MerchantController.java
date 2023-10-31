package com.payment.merchant.controller;


import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import com.payment.merchant.dto.*;
import com.payment.merchant.model.Merchant;
import com.payment.merchant.service.MerchantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("api/v1/users/auth")
public class MerchantController {
    private final MerchantService merchantService;

    private final GeneralService generalService;

    public MerchantController(MerchantService merchantService, GeneralService generalService) {

        this.merchantService = merchantService;
        this.generalService = generalService;
    }

    @PostMapping("/sign-up")  // create user api
    public Response signUp(@RequestBody CreateUpdateUserDTO signUpRequest) {

        AdminUserDTO data = merchantService.addUser(signUpRequest);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    @PostMapping("/sign-in")   // log in api
    public ResponseEntity<Response> signIn(@RequestBody SignInRequest request) {
        Response data = merchantService.signIn(request.getUsername(), request.getPassword());
        if (data.getResponseCode().equals(ResponseCodeAndMessage.SUCCESSFUL_0.responseCode))
            return new ResponseEntity<>(data, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{username}")
    public Response getOne(@PathVariable String username) {

        Merchant data = merchantService.get(username);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

}
