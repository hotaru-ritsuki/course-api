package com.example.courseapi.controller.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

/**
 * Utility class to help building response body
 */
public final class ResponseUtil {
    private ResponseUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Wraps response if present, otherwise returns Not Found Response
     * @param responseOpt Optional response body
     * @return response body
     */
    public static <X> ResponseEntity<X> wrapOrNotFound(final Optional<X> responseOpt) {
        return wrapOrNotFound(responseOpt, null);
    }

    /**
     * Wraps response if present, otherwise returns Not Found Response
     * @param responseOpt Optional response body
     * @param header additional headers
     * @return response body
     */
    public static <X> ResponseEntity<X> wrapOrNotFound(final Optional<X> responseOpt, final HttpHeaders header) {
        return responseOpt.map((response) -> ResponseEntity.ok()
                    .headers(header).body(response))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)
                );
    }
}