package com.payment.config.security;

import com.google.gson.Gson;
import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    private final Gson gson;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Response responseObject = getResponseObject();
        String responsePayload = gson.toJson(responseObject);
        response.getWriter().write(responsePayload);
    }

    private Response getResponseObject(){
        Response response = new Response();
        response.setResponseMessage(ResponseCodeAndMessage.UNAUTHORIZED_ACCESS.responseMessage);
        response.setResponseCode(ResponseCodeAndMessage.UNAUTHORIZED_ACCESS.responseCode);
        response.setData("Access Denied. You are not authorized to access this resource");
        return response;
    }
}
