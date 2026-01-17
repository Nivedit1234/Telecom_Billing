package com.telecom.billing.telecom_billing.Controllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO used for creating new Customer objects from incoming API requests.
 * Validation annotations ensure the incoming JSON contains valid data.
 */
public class CreateCustomerDto {

    // ------------------------------------------------------------
    // Customer's full name
    // Must not be blank and must have at least 2 characters
    // ------------------------------------------------------------
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, message = "Name must have at least 2 characters")
    private String name;

    // ------------------------------------------------------------
    // Customer phone number
    // Must not be blank (controller will also check duplicates)
    // ------------------------------------------------------------
    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

    // ------------------------------------------------------------
    // Customer email address
    // Optional, but if provided must be a valid email format
    // ------------------------------------------------------------
    @Email(message = "Invalid email format")
    private String email;

    // Getter: returns the customer's name
    public String getName() { return name; }

    // Setter: assigns the name when request JSON comes in
    public void setName(String name) { this.name = name; }

    // Getter for phone number
    public String getPhoneNumber() { return phoneNumber; }

    // Setter for phone number
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    // Getter for email
    public String getEmail() { return email; }

    // Setter for email
    public void setEmail(String email) { this.email = email; }
}
