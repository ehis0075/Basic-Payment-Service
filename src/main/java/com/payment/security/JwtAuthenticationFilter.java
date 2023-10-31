package com.payment.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse httpServletResponse, AuthenticationException authException) throws IOException {
        // Set the content type of the httpServletResponse to indicate it's JSON
        httpServletResponse.setContentType("application/json");

        Response response = new Response();
        response.setResponseCode(ResponseCodeAndMessage.UNAUTHORIZED_97.responseCode);
        response.setResponseMessage(ResponseCodeAndMessage.UNAUTHORIZED_97.responseMessage);

        // Convert the error httpServletResponse to JSON and write it to the httpServletResponse body
        String jsonResponse = new ObjectMapper().writeValueAsString(response);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.getWriter().write(jsonResponse);
    }
}
