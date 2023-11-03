package com.prophius.socialmediaservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class JwtSecretUtils {
    private final ObjectMapper objectMapper;

    @Value("${jwt.signing.key:}")
    private String value;

    @Value("${jwt.signing.issuer:}")
    private String issuer;

    private int expiration;

    private int refreshTokenNBF = 30;

    private int refreshTokenExpirationInDays = 14;
}
