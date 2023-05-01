package com.example.courseapi.exception.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ApiExceptionDTO {
    private String code;
    private String message;
    private String stackTrace;
    private Map<String, Object> properties;

    public ApiExceptionDTO code(final String code) {
        this.code = code;
        return this;
    }
}