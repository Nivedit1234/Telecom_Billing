package com.telecom.billing.telecom_billing.Services;

import com.telecom.billing.telecom_billing.Controllers.CreateUserForCustomerDto;
import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Models.Role;
import com.telecom.billing.telecom_billing.Models.User;
import com.telecom.billing.telecom_billing.Repository.CustomerRepository;
import com.telecom.billing.telecom_billing.Repository.UserRepository;
import com.telecom.billing.telecom_billing.exception.ConflictException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * AdminService - handles admin-only tasks such as creating user+customer and linking them.
 * The createUserForCustomer method is transactional so both creations (user + customer + link)
 * succeed or the whole transaction rolls back.
 */
@Service
public class AdminService {

    private final UserRepository userRepo;
    private final CustomerRepository customerRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepo,
                        CustomerRepository customerRepo,
                        BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create (or link) a User and Customer together.
     * Behavior:
     *  - If a user with username=phone exists -> throws ConflictException
     *  - If customer exists and already linked -> throws ConflictException
     *  - If customer missing -> creates it
     *  - Creates user, links to customer, saves both (atomic)
     */
    @Transactional
    public AdminResult createUserForCustomer(CreateUserForCustomerDto dto) {
        String phone = dto.getPhoneNumber().trim();
        if (phone.isEmpty()) throw new IllegalArgumentException("phoneNumber is required");

        // 1) If username already exists -> conflict
        if (userRepo.existsByUsername(phone)) {
            throw new ConflictException("User already exists with username/phone: " + phone);
        }

        // 2) Find or create customer
        Customer customer = customerRepo.findByPhoneNumber(phone).orElse(null);
        if (customer != null && customer.getUser() != null) {
            throw new ConflictException("Customer already linked to a user (customer id=" + customer.getId() + ")");
        }

        if (customer == null) {
            customer = new Customer();
            customer.setPhoneNumber(phone);
            customer.setName(dto.getName() != null ? dto.getName() : "");
            customer.setEmail(dto.getEmail());
            try {
                customer = customerRepo.save(customer);
            } catch (Exception ex) {
                // map DB exception to conflict
                throw new ConflictException("Failed to create customer: " + ex.getMessage(), ex);
            }
        }

        // 3) Create user
        User u = new User();
        u.setUsername(phone);
        u.setEmail(dto.getEmail() != null ? dto.getEmail() : (customer.getEmail() != null ? customer.getEmail() : phone + "@example.com"));
        u.setPassword(passwordEncoder.encode(dto.getPassword())); // hash
        u.getRoles().add(Role.ROLE_USER);
        u.setEnabled(true);

        try {
            u = userRepo.save(u);
        } catch (Exception ex) {
            // If user save fails, transaction will roll back and customer create (if done) is undone
            throw new ConflictException("Failed to create user: " + ex.getMessage(), ex);
        }

        // 4) Link and save customer
        try {
            customer.setUser(u);
            customer = customerRepo.save(customer);
        } catch (Exception ex) {
            throw new ConflictException("Failed to link user to customer: " + ex.getMessage(), ex);
        }

        Set<String> roles = u.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        return new AdminResult(u.getId(), u.getUsername(), customer.getId(), roles);
    }

    // Simple result record (can be replaced by AdminResponse DTO in controller)
    public static record AdminResult(Long userId, String username, Long customerId, Set<String> roles) {}
}
