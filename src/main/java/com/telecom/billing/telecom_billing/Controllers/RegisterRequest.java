package com.telecom.billing.telecom_billing.Controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * RegisterRequest DTO
 * --------------------
 * This class represents the JSON payload received during user registration.
 *
 * When a user registers:
 *  - A new User (authentication account) is created
 *  - A Customer (telecom profile) is created or linked
 *
 * DTO keeps API input separate from database entities.
 */
public class RegisterRequest {

    /**
     * The phone number is the PRIMARY identity used for login.
     * Must not be blank.
     */
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    /**
     * Raw password supplied by the user.
     * Validation ensures a minimum length before hashing.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    /**
     * Optional customer full name.
     * Used for telecom customer profile creation.
     */
    private String name;

    /**
     * Optional customer email.
     * If missing → backend auto-generates an email.
     */
    private String email;

    // --------------------
    // Getters & Setters
    // --------------------

    public String getPhoneNumber() { 
        return phoneNumber; 
    }

    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }

    public String getPassword() { 
        return password; 
    }

    public void setPassword(String password) { 
        this.password = password; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }
}
