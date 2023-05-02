package com.example.courseapi.exception;

import org.springframework.security.access.AccessDeniedException;

import java.io.Serial;

public class AccessValidationException extends AccessDeniedException {
    @Serial
    private static final long serialVersionUID = 4163447624909949873L;

    public AccessValidationException(final String msg) {
        super(msg);
    }

    public AccessValidationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
