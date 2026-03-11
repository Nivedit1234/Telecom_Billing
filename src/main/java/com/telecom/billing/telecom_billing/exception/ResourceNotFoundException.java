package com.telecom.billing.telecom_billing.exception;


/**
 * Thrown when a requested resource (Customer / Plan / Usage / Invoice / User) is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
