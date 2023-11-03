package com.prophius.socialmediaservice.config.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.Stack;

@Setter
@Getter
public class AntMatchersEndpoints {

    private Stack<AntMatchersEndpoint> antMatchersEndpoints;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AntMatchersEndpoint {
        private HttpMethod method;
        private String path;
        private boolean permitAll = true;
        private String[] authorities = {};

        public AntMatchersEndpoint(HttpMethod method, String path) {
            setPermitAll(true);
            this.method = method;
            this.path = path;
        }

        public AntMatchersEndpoint(String path, String... authorities) {
            setMethod(null);
            setPermitAll(false);
            this.path = path;
            this.authorities = authorities;
        }
    }
}
