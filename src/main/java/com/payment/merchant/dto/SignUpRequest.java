package com.payment.merchant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

//    @NotBlank(message = "email is required")
//    @Pattern(regexp = "^(.+)@(.+)$", message = "username must be a valid email address ")
    private String email;

//    @NotBlank(message = "username is required")
    private String username;

//    @NotBlank(message = "password is required")
    private String password;

    private String base64; // base 64

    private String imageUrl;
}
