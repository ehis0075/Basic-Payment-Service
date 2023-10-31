package com.payment.merchant.dto;

import com.payment.role.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignInResponse {
    private String username;
    private Role userRole;
    private String accessToken;
}
