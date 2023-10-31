package com.payment.merchant.imodel;

import org.springframework.beans.factory.annotation.Value;

public interface UserBasicInfoI {
    @Value(value = "#{target.id}")
    Long id();

    @Value(value = "#{target.email}")
    String email();

    @Value(value = "#{target.username}")
    String username();

}
