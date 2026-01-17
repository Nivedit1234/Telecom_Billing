package com.telecom.billing.telecom_billing.Controllers;

import java.util.Set;

/**
 * Response returned after successful create/link.
 */
public class AdminResponse {
    private final String username;
    private final Long userId;
    private final Long customerId;
    private final Set<String> roles;

    public AdminResponse(String username, Long userId, Long customerId, Set<String> roles) {
        this.username = username;
        this.userId = userId;
        this.customerId = customerId;
        this.roles = roles;
    }

    public String getUsername() { return username; }
    public Long getUserId() { return userId; }
    public Long getCustomerId() { return customerId; }
    public Set<String> getRoles() { return roles; }
}
