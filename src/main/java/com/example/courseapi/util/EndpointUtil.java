package com.example.courseapi.util;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

/**
 * Utility class to help retrieving request information
 */
public final class EndpointUtil {
    private EndpointUtil() {
        throw new IllegalStateException("Can not create instance of utility class");
    }

    /**
     * Get client IP from request
     * @param request current request
     * @return client IP
     */
    public static String getClientIP(final HttpServletRequest request){
        String ip = request.getHeader("True-Client-IP");
        if (StringUtils.isBlank(ip)){
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isBlank(ip)){
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * Get User-Agent header from request
     * @param request current request
     * @return User-Agent header value
     */
    public static String getUserAgent(final HttpServletRequest request){
        return request.getHeader(HttpHeaders.USER_AGENT);
    }
}
