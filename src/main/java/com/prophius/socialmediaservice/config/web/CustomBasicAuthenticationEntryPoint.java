package com.prophius.socialmediaservice.config.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prophius.socialmediaservice.dto.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.prophius.socialmediaservice.util.Constants.PROCESSED_AUTH_TOKEN;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
            throws IOException {
        String error = "Basic Authentication - %s";
        if (request.getAttribute(PROCESSED_AUTH_TOKEN) != null) {
            request.removeAttribute(PROCESSED_AUTH_TOKEN);
            error = "JWT - %s";
        }

        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.addError(format(error, authEx.getMessage()));
        PrintWriter writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(errorResponseDto));
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("PROPHIUS_API");
        super.afterPropertiesSet();
    }
}