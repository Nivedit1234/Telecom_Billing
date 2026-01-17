package com.telecom.billing.telecom_billing.Controllers;

/**
 * Request DTO used by admin to create a user/customer.
 * Fields:
 *  - phoneNumber (required) : used as username
 *  - password (required)    : temporary password for created user
 *  - name (optional)        : customer name
 *  - email (optional)       : user/customer email
 */
public class CreateUserForCustomerDto {
    private String phoneNumber;
    private String password;
    private String name;
    private String email;

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
