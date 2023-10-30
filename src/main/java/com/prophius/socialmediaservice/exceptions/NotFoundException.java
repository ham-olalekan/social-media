package com.prophius.socialmediaservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends Exception{

    private String message;

    private HttpStatus status;

    public NotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
