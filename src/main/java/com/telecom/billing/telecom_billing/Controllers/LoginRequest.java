package com.telecom.billing.telecom_billing.Controllers;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO (Data Transfer Object) for accepting login input from the API client.
 *
 * This class represents only the required fields for login:
 *   - phoneNumber (used in our telecom system as username)
 *   - password
 *
 * Validation is performed automatically by Spring:
 *   - @NotBlank ensures fields are not null, empty, or whitespace-only
 *
 * Keeping DTOs separate from entities improves security
 * and prevents exposing internal database models to the client.
 */
public class LoginRequest {

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;

    // Getter and Setter for phoneNumber
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
