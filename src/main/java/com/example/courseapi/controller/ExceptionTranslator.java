package com.example.courseapi.controller;

import com.example.courseapi.dto.ApiExceptionDTO;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Log4j2
public class ExceptionTranslator {

    @Value("${exception.show-stack-trace:false}")
    private Boolean showStackTrace;

    /**
     * Exception handler for {@link NoHandlerFoundException}
     * @param ex exception to handle
     * @param httpServletRequest current request
     * @return returns not found response if the url starts with api otherwise redirect to page not found
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handlerNotFound(final NoHandlerFoundException ex, final HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getRequestURI().startsWith("/api"))
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        else {
            return "redirect:/#/page-not-found";
        }
    }

    /**
     * Exception handler for {@link ConcurrencyFailureException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link ConcurrencyFailureException}
     */
    @ExceptionHandler(ConcurrencyFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiExceptionDTO processConcurrencyError(final ConcurrencyFailureException ex) {
        log.error("ConcurrencyFailureException:", ex);
        return this.exceptionToApiError(ex).code(valueOf(HttpStatus.CONFLICT.value()));
    }

    /**
     * Exception handler for {@link ConcurrencyFailureException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link ConcurrencyFailureException}
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiExceptionDTO processBadCredentialsException(final BadCredentialsException ex) {
        log.error("BadCredentialsException:", ex);
        return this.exceptionToApiError(ex).code(valueOf(HttpStatus.UNAUTHORIZED.value()));
    }

    /**
     * Exception handler for {@link MethodArgumentNotValidException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link MethodArgumentNotValidException}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ApiExceptionDTO> processValidationError(final MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException:", ex);

        final List<ApiExceptionDTO> errorMessages = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(e -> {
            final ApiExceptionDTO apiException = this.exceptionToApiError(ex).code(valueOf(HttpStatus.BAD_REQUEST.value()));
            apiException.setMessage(e.getDefaultMessage());
            errorMessages.add(apiException);
        });

        return errorMessages;
    }

    /**
     * Exception handler for {@link IllegalArgumentException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link IllegalArgumentException}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionDTO processIllegalArgumentException(final IllegalArgumentException ex) {
        log.error("IllegalArgumentException:", ex);
        return this.exceptionToApiError(ex).code(valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Exception handler for {@link AccessDeniedException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link AccessDeniedException}
     */
    @ExceptionHandler({AccessDeniedException.class, AuthenticationCredentialsNotFoundException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ApiExceptionDTO processAccessDeniedException(final RuntimeException ex) {
        log.error("AccessDeniedException:", ex);
        return this.exceptionToApiError(ex).code(valueOf(HttpStatus.FORBIDDEN.value()));
    }

    /**
     * Exception handler for {@link HttpRequestMethodNotSupportedException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link HttpRequestMethodNotSupportedException}
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiExceptionDTO processMethodNotSupportedException(final HttpRequestMethodNotSupportedException ex) {
        log.error("AccessDeniedException:", ex);
        return this.exceptionToApiError(ex).code(valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()));
    }

    /**
     * Exception handler for {@link ConstraintViolationException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link ConstraintViolationException}
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ApiExceptionDTO> processConstraintViolationException(final ConstraintViolationException ex) {

        log.error("ConstraintViolationException:", ex);

        final List<ApiExceptionDTO> errorMessages = new ArrayList<>();

        ex.getConstraintViolations().forEach(e -> {
            final ApiExceptionDTO apiException = this.exceptionToApiError(ex).code(valueOf(HttpStatus.BAD_REQUEST.value()));
            apiException.setMessage(e.getMessage());
            errorMessages.add(apiException);
        });

        return errorMessages;
    }

    /**
     * Exception handler for {@link SystemException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link SystemException}
     */
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ApiExceptionDTO> handleCustomException(final SystemException ex) {
        log.error(ex.getMessage(), ex);
        final ResponseEntity.BodyBuilder builder = ResponseEntity.status(ex.getErrorCode().getHttpStatus());
        final ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO();
        apiExceptionDTO.setMessage(ex.getMessage());
        apiExceptionDTO.setCode(ex.getErrorCode().getHttpStatus().name());
        apiExceptionDTO.setProperties(ex.get());
        return builder.body(apiExceptionDTO);
    }

    /**
     * Exception handler for {@link HttpMessageConversionException} and {@link MethodArgumentTypeMismatchException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link HttpMessageConversionException} and {@link MethodArgumentTypeMismatchException}
     */
    @ExceptionHandler({HttpMessageConversionException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ApiExceptionDTO> handleEmptyPostBody(final NestedRuntimeException ex) {
        final ApiExceptionDTO apiException = this.exceptionToApiError(ex).code(valueOf(ErrorCode.BAD_REQUEST.getHttpStatus().value()));

        // if we get an error from @JsonCreator, ex.getCause().getCause() is SystemException
        if (ex.getMostSpecificCause() instanceof SystemException){
            apiException.setMessage(ex.getMostSpecificCause().getMessage());
        }

        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getHttpStatus()).body(apiException);
    }

    /**
     * Exception handler for {@link HttpMessageNotReadableException}
     * @param ex exception to handle
     * @return returns user-friendly json response for {@link HttpMessageNotReadableException}
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ApiExceptionDTO> handleHttpBodyNotReadable(HttpMessageNotReadableException ex) {
        final ApiExceptionDTO apiException = this.exceptionToApiError(ex).code(valueOf(ErrorCode.BAD_REQUEST.getHttpStatus().value()));
        apiException.setMessage("An exception arise while trying to parse request body");
        apiException.setProperties(Map.of("error", ex.getLocalizedMessage()));
        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getHttpStatus()).body(apiException);
    }

    /**
     * Util method to convert any exception type to user-friendly {@link ApiExceptionDTO}
     * @param ex exception to convert
     * @return user-friendly {@link ApiExceptionDTO}
     */
    private ApiExceptionDTO exceptionToApiError(final Exception ex) {
        final ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO();
        apiExceptionDTO.setMessage(ex.getMessage());
        if (this.showStackTrace) {
            apiExceptionDTO.setStackTrace(ExceptionUtils.getStackTrace(ex));
        }

        return apiExceptionDTO;
    }
}
