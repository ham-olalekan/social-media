package com.prophius.socialmediaservice.service;

import com.prophius.socialmediaservice.dto.CreateUserDto;
import com.prophius.socialmediaservice.dto.FUserDto;
import com.prophius.socialmediaservice.dto.UserDto;
import com.prophius.socialmediaservice.exceptions.CommonsException;

public interface UserService {
    UserDto registerNewUser(CreateUserDto signupDto);

    FUserDto findByUserId(long userId) throws CommonsException;
}
