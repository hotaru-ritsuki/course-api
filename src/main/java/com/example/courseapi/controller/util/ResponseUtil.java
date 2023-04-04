package com.example.courseapi.controller.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public final class ResponseUtil {
    private ResponseUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(final Optional<X> maybeResponse) {
        return wrapOrNotFound(maybeResponse, null);
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(final Optional<X> maybeResponse, final HttpHeaders header) {
        return maybeResponse.map((response) -> ResponseEntity.ok()
                    .headers(header).body(response))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)
                );
    }
}