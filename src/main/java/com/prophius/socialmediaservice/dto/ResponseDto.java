package com.prophius.socialmediaservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prophius.socialmediaservice.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ResponseDto<T> {
    private ResponseStatus status;
    private T data;
    private String message;

    @JsonIgnore
    private Object[] messageArgs;

    public static <T> ResponseDto<T> wrapSuccessResult(T data, String message) {
        ResponseDto<T> response = new ResponseDto<>();
        response.setData(data);
        response.setMessage(message);
        response.setStatus(ResponseStatus.SUCCESS);
        return response;
    }

    public static <T> ResponseDto<T> wrapErrorResult(String message) {
        ResponseDto<T> response = new ResponseDto<>();
        response.setStatus(ResponseStatus.ERROR);
        response.setMessage(message);
        return response;
    }

    public static <T> ResponseDto<T> wrapPermissionErrorResult(String message) {
        ResponseDto<T> response = new ResponseDto<>();
        response.setStatus(ResponseStatus.PERMISSION_ERROR);
        response.setMessage(message);
        return response;
    }
}
