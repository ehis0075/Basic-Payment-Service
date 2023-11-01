package com.payment.config;

import com.payment.config.security.AccessDeniedHandlerImpl;
import com.payment.config.security.AuthenticationEntryPointImpl;
import com.payment.config.security.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@AllArgsConstructor
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;

    private PasswordEncoder passwordEncoder;

    private AccessDeniedHandlerImpl accessDeniedHandler;

    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource()).and().csrf().disable().authorizeRequests()
                .antMatchers("/api/v1/auth/**",
                        "/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security",
                        "/swagger-ui.html", "/webjars/**", "/v2/api-docs", "/v3/api-docs", "/swagger-ui/**")
                .permitAll()
                .and().exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return new ProviderManager(createAuthenticationProvider());
    }

    public AuthenticationProvider createAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("http://**", "https://**"));

        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setExposedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
