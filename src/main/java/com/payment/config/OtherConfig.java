package com.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

@Configuration
public class OtherConfig {


    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }
}
