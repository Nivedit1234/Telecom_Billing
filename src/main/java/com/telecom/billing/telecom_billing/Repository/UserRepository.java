package com.telecom.billing.telecom_billing.Repository;

import com.telecom.billing.telecom_billing.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by username (used during login/authentication)
    Optional<User> findByUsername(String username);

    // Check if a user already exists with this username (prevents duplicate registration)
    boolean existsByUsername(String username);

    // Check if a user already exists with this email (enforces unique email constraint)
    boolean existsByEmail(String email);
}
