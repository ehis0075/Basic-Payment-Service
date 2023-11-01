package com.payment.auth.service;


import com.payment.auth.model.UserSecurityDetails;

public interface UserSecurityDetailsService {
    UserSecurityDetails createUserSecurityDetails(Object requestObject);
    boolean validateEmail(String email);

    UserSecurityDetails saveUserSecurityDetails(UserSecurityDetails userSecurityDetails);
}
