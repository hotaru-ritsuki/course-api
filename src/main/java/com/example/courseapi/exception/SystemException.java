package com.example.courseapi.exception;


import com.example.courseapi.exception.code.ErrorCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SystemException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7153808129776996908L;

    private final ErrorCode errorCode;
    private final HashMap<String, Object> properties = new LinkedHashMap<>();

    public SystemException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public SystemException(final String message, final ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public SystemException(final String message, final Throwable exception, final ErrorCode errorCode) {
        super(message, exception);
        this.errorCode = errorCode;
    }

    public SystemException(final Throwable exception, final ErrorCode errorCode) {
        super(exception);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> get() {
        return properties;
    }

    public SystemException set(final String key, final Object value) {
        properties.put(key, value);
        return this;
    }

    @Override
    public String getMessage() {
        final String message = super.getMessage();
        return StringUtils.isEmpty(message) ? toString() : message;
    }

    @Override
    public String toString() {
        return "Original message: " + super.getMessage() + "\n Error Code: " +
                errorCode + "\n properties: " + properties;
    }
}
