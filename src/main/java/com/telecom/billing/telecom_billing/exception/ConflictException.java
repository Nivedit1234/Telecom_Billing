package com.telecom.billing.telecom_billing.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Thrown when a request cannot be completed because of a conflict —
 * for example duplicate phone number, email, or violating a unique constraint.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {

    private static final long serialVersionUID = 1L; // removes the serial warning

    public ConflictException() { super(); }
    public ConflictException(String message) { super(message); }
    public ConflictException(String message, Throwable cause) { super(message, cause); }
    public ConflictException(Throwable cause) { super(cause); }}
