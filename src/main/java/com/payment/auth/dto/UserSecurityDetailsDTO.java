package com.payment.auth.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserSecurityDetailsDTO {

    private Long id;
    private String email;
    private String password;
    private String userType;
    private boolean enabled;
    private boolean locked;
    private String roleName;
    private Long roleId;
    private Set<String> authorities;
    private boolean isLoggedIn;
}
