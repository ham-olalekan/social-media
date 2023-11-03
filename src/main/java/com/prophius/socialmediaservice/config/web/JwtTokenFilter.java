package com.prophius.socialmediaservice.config.web;

import com.prophius.socialmediaservice.dals.IUserDetails;
import com.prophius.socialmediaservice.exceptions.CommonsException;
import com.prophius.socialmediaservice.util.JwtTokenUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.prophius.socialmediaservice.util.Constants.PROCESSED_AUTH_TOKEN;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest,
                                    @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Get authorization header and validate
        final String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (!isEmpty(header) && header.startsWith("Bearer ")) {
            httpServletRequest.setAttribute(PROCESSED_AUTH_TOKEN, true);
            try {
                // Get jwt token and validate
                String[] headerSplit = header.split(" ");
                final String token = headerSplit[1].trim();
                JwtTokenUtils.JwtDto jwtDTO = jwtTokenUtils.getValidJwtDetails(token, JwtTokenUtils.TokenType.ACCESS);
                if (jwtDTO.valid) {
                    IUserDetails iUserDetails = jwtDTO.asUser();
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(iUserDetails, null,
                                    iUserDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                    return;
                }
            } catch (CommonsException ignored) {
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
