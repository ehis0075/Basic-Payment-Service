package com.payment.auth.controller;


import com.payment.auth.dto.LoginRequestDto;
import com.payment.auth.dto.LoginResponseDTO;
import com.payment.auth.service.AuthService;
import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final GeneralService generalService;

    @PostMapping("/login")
    public Response authenticateUser(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDTO data = authService.authenticateUser(loginRequestDto);
        Response response = generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
        log.info(response.toString());
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    @PostMapping("/logOut")
    public Response signOut(Principal principal) {
        boolean data = authService.logout(principal);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }
}
