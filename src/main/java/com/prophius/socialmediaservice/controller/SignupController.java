package com.prophius.socialmediaservice.controller;

import com.prophius.socialmediaservice.dals.IUserDetails;
import com.prophius.socialmediaservice.dto.CreateUserDto;
import com.prophius.socialmediaservice.dto.TokensDto;
import com.prophius.socialmediaservice.dto.UserDto;
import com.prophius.socialmediaservice.exceptions.CommonsException;
import com.prophius.socialmediaservice.service.UserService;
import com.prophius.socialmediaservice.util.JwtTokenUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.prophius.socialmediaservice.util.Constants.X_ACCESS_TOKEN;
import static com.prophius.socialmediaservice.util.Constants.X_REFRESH_TOKEN;

@Log4j2
@Validated
@RestController
@RequiredArgsConstructor
public class SignupController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/api/v1/user/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody CreateUserDto signupDto, HttpServletResponse httpServletResponse) throws CommonsException {
        UserDto userDto = userService.registerNewUser(signupDto);

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), signupDto.getPassword()));
        TokensDto tokensDto = jwtTokenUtils.generateTokens((IUserDetails) authenticate.getPrincipal());
        httpServletResponse.addHeader(X_ACCESS_TOKEN, tokensDto.getAccessToken());
        httpServletResponse.addHeader(X_REFRESH_TOKEN, tokensDto.getRefreshToken());
    }
}
