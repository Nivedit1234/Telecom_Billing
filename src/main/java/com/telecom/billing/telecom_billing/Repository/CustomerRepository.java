package com.telecom.billing.telecom_billing.Repository;

import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Models.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Find a customer using their email (returns Optional because record may not exist)
    Optional<Customer> findByEmail(String email);

    // Find a customer using their phone number (also unique)
    Optional<Customer> findByPhoneNumber(String phoneNumber);

    // Find a customer by the linked User's ID (Customer.user → User.id)
    Optional<Customer> findByUserId(Long userId);

    // Find a customer using the linked User's username
    Optional<Customer> findByUser_Username(String username);

    // Find a customer using the User object directly
    Optional<Customer> findByUser(User user);

}
