package com.payment.auth.service;


import com.payment.auth.dto.LoginRequestDto;
import com.payment.auth.dto.LoginResponseDTO;

import java.security.Principal;

public interface AuthService {
    LoginResponseDTO authenticateUser(LoginRequestDto loginRequestDto);

    boolean logout(Principal principal);
}
