package com.payment.user.imodel;

import org.springframework.beans.factory.annotation.Value;

public interface MerchantUserAuth {
    @Value(value = "#{target.email}")
    String email();

    @Value(value = "#{target.role_id}")
    Long role_id();
}
