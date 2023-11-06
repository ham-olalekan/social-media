package com.prophius.socialmediaservice.config;

import com.prophius.socialmediaservice.config.web.AntMatchersEndpoints;
import com.prophius.socialmediaservice.config.web.CustomAccessDeniedHandler;
import com.prophius.socialmediaservice.config.web.CustomBasicAuthenticationEntryPoint;
import com.prophius.socialmediaservice.config.web.JwtTokenFilter;
import com.prophius.socialmediaservice.security.IPasswordEncoder;
import com.prophius.socialmediaservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Stack;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final IPasswordEncoder passwordEncoder;

    private final AntMatchersEndpoints antMatchersPermittedEndpoints;

    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final JwtTokenFilter jwtTokenFilter;

    private final IUserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
                expressionInterceptUrlRegistry = http.authorizeRequests();

        // use antMatchers provided by the module
        if (antMatchersPermittedEndpoints != null) {
            Stack<AntMatchersEndpoints.AntMatchersEndpoint> antMatchersEndpoints =
                    antMatchersPermittedEndpoints.getAntMatchersEndpoints();
            while (!antMatchersEndpoints.empty()) {
                AntMatchersEndpoints.AntMatchersEndpoint antMatchersEndpoint = antMatchersEndpoints.pop();
                HttpMethod method = antMatchersEndpoint.getMethod();
                if (antMatchersEndpoint.isPermitAll()) {
                    if (method == null) {
                        expressionInterceptUrlRegistry.antMatchers(antMatchersEndpoint.getPath()).permitAll();
                        continue;
                    }
                    expressionInterceptUrlRegistry.antMatchers(antMatchersEndpoint.getMethod(),
                            antMatchersEndpoint.getPath()).permitAll();
                } else if (antMatchersEndpoint.getAuthorities().length > 0) {
                    if (antMatchersEndpoint.getMethod() != null) {
                        expressionInterceptUrlRegistry
                                .antMatchers(antMatchersEndpoint.getMethod(), antMatchersEndpoint.getPath())
                                .hasAnyAuthority(antMatchersEndpoint.getAuthorities());
                        continue;
                    }
                    expressionInterceptUrlRegistry.antMatchers(antMatchersEndpoint.getPath())
                            .hasAnyAuthority(antMatchersEndpoint.getAuthorities());
                } else {
                    expressionInterceptUrlRegistry.antMatchers(antMatchersEndpoint.getMethod(),
                            antMatchersEndpoint.getPath()).denyAll();
                }
            }
        }

        http = expressionInterceptUrlRegistry.anyRequest().authenticated().and();
        http.httpBasic().authenticationEntryPoint(customBasicAuthenticationEntryPoint);
        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }
}