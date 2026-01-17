package com.telecom.billing.telecom_billing.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when the request is invalid or cannot be processed.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public BadRequestException() { super(); }
    public BadRequestException(String message) { super(message); }
    public BadRequestException(String message, Throwable cause) { super(message, cause); }
}
