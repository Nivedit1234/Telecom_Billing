package com.telecom.billing.telecom_billing.Controllers;

import java.util.Set;

/**
 * DTO (Data Transfer Object) used to send the login response
 * back to the client after successful authentication.
 *
 * This object contains:
 *
 *  🔹 JWT token — used for accessing protected APIs
 *  🔹 Username — helps the frontend identify the logged-in user
 *  🔹 Roles — defines user's permissions for authorization checks
 *
 * This class has no business logic — it is simply a clean, safe
 * structure for exposing authentication results to the client.
 */
public class LoginResponse {

    // JWT token generated after successful login
    private final String token;

    // Username of the authenticated user
    private final String username;

    // User roles (e.g., ROLE_USER, ROLE_ADMIN)
    private final Set<String> roles;

    /**
     * Constructor to initialize all fields.
     * Since the response should be immutable,
     * we do not expose setters.
     */
    public LoginResponse(String token, String username, Set<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

    // ----- Getters -----

    /** @return JWT token */
    public String getToken() {
        return token;
    }

    /** @return username */
    public String getUsername() {
        return username;
    }

    /** @return user roles */
    public Set<String> getRoles() {
        return roles;
    }
}
