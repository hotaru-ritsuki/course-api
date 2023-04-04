package com.example.courseapi.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * Helper class for HTTP headers creation.
 */
@Component
@Log4j2
public final class EntityHeaderCreator {

    @Value("${application.name}")
    private String APPLICATION_NAME;

    public HttpHeaders createAlert(final String message, final String param) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-alert", message);
        headers.add("X-params", param);
        return headers;
    }

    public HttpHeaders createEntityCreationAlert(final String entityName, final String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
    }

    public HttpHeaders createEntityUpdateAlert(final String entityName, final String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
    }

    public HttpHeaders createEntityDeletionAlert(final String entityName, final String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
    }

    public HttpHeaders createFailureAlert(final String entityName, final String errorKey, final String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-error", "error." + errorKey);
        headers.add("X-params", entityName);
        return headers;
    }
}