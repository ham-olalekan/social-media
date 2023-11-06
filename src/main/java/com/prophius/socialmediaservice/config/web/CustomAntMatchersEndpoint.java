package com.prophius.socialmediaservice.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Stack;

import static org.springframework.http.HttpMethod.POST;

@Configuration
@RequiredArgsConstructor
public class CustomAntMatchersEndpoint {

    private final AntMatchersEndpoints antMatchersEndpoints;

    @Bean
    @Primary
    public AntMatchersEndpoints antMatchersEndpoints() {
        Stack<AntMatchersEndpoints.AntMatchersEndpoint> antMatchersEndpointSet
                = antMatchersEndpoints.getAntMatchersEndpoints();
        antMatchersEndpointSet.add(new AntMatchersEndpoints
                .AntMatchersEndpoint(POST, "/api/v1/user/signup"));
        return antMatchersEndpoints;
    }

}
