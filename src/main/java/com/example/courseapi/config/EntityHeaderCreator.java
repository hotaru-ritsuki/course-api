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

    private static final String X_ERROR_HEADER = "X-error";
    private static final String X_PARAMS_HEADER = "X-params";
    private static final String X_ALERT_HEADER = "X-alert";

    @Value("${application.name}")
    private String APPLICATION_NAME;

    /**
     * Creates alert headers used for response
     * @param message X-alert header
     * @param param X-params header
     * @return Alert HttpHeaders used for response
     */
    public HttpHeaders createAlert(final String message, final String param) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(X_ALERT_HEADER, message);
        headers.add(X_PARAMS_HEADER, param);
        return headers;
    }

    /**
     * Creates entity creation alert headers used for response
     * @param entityName given entity name
     * @param param additional param
     * @return Alert entity creation HttpHeaders used for response
     */
    public HttpHeaders createEntityCreationAlert(final String entityName, final String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
    }

    /**
     * Creates entity update alert headers used for response
     * @param entityName given entity name
     * @param param additional param
     * @return Alert entity update HttpHeaders used for response
     */
    public HttpHeaders createEntityUpdateAlert(final String entityName, final String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
    }

    /**
     * Creates entity deletion alert headers used for response
     * @param entityName given entity name
     * @param param additional param
     * @return Alert deletion creation HttpHeaders used for response
     */
    public HttpHeaders createEntityDeletionAlert(final String entityName, final String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
    }

    /**
     * Creates entity failure alert headers used for response
     * @param entityName given entity name used for X-params header
     * @param errorKey X-error error key header
     * @param defaultMessage default message
     * @return Alert entity failure HttpHeaders used for response
     */
    public HttpHeaders createFailureAlert(final String entityName, final String errorKey, final String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(X_ERROR_HEADER, "error." + errorKey);
        headers.add(X_PARAMS_HEADER, entityName);
        return headers;
    }
}