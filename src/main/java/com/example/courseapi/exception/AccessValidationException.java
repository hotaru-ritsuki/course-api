package com.example.courseapi.exception;

import org.springframework.security.access.AccessDeniedException;

public class AccessValidationException extends AccessDeniedException {
    public AccessValidationException(String msg) {
        super(msg);
    }

    public AccessValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
