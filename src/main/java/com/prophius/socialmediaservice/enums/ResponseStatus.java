package com.prophius.socialmediaservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    SUCCESS("SUCCESS"),

    PERMISSION_ERROR("PERMISSION_ERROR"),
    ERROR("ERROR");

    private String status;
}
