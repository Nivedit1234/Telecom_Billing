package com.telecom.billing.telecom_billing.Services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.telecom.billing.telecom_billing.Controllers.RegisterRequest;
import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Models.Role;
import com.telecom.billing.telecom_billing.Models.User;
import com.telecom.billing.telecom_billing.Repository.CustomerRepository;
import com.telecom.billing.telecom_billing.Repository.PlanRepository;
import com.telecom.billing.telecom_billing.Repository.UserRepository;
import com.telecom.billing.telecom_billing.exception.ConflictException;
import com.telecom.billing.telecom_billing.exception.ResourceNotFoundException;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final CustomerRepository customerRepo;
    private final PlanRepository planRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor Injection (Best Practice)
    public AuthService(UserRepository userRepo,
                       CustomerRepository customerRepo,
                       PlanRepository planRepo,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.customerRepo = customerRepo;
        this.planRepo = planRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Handles registration of both:
     * - User (authentication entity)
     * - Customer (telecom profile)
     *
     * Also assigns the selected telecom plan during registration.
     */
    @Transactional
    public RegisterResult register(RegisterRequest req, Long planId) {

        String phone = req.getPhoneNumber().trim();

        // ----------------------------------------------------
        // STEP 1 — Check if user already exists
        // ----------------------------------------------------
        if (userRepo.existsByUsername(phone)) {
            throw new ConflictException("User already exists with this phone number");
        }

        // ----------------------------------------------------
        // STEP 2 — Fetch telecom plan
        // ----------------------------------------------------
        Plan plan = planRepo.findById(planId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Plan not found with id: " + planId));

        // ----------------------------------------------------
        // STEP 3 — Create User entity
        // ----------------------------------------------------
        User user = new User();
        user.setUsername(phone);
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));

        user = userRepo.save(user);

        // ----------------------------------------------------
        // STEP 4 — Create Customer entity
        // ----------------------------------------------------
        Customer customer = new Customer();
        customer.setName(req.getName());
        customer.setEmail(req.getEmail());
        customer.setPhoneNumber(phone);

        // Assign selected telecom plan
        customer.setPlan(plan);

        // Link authentication user
        customer.setUser(user);

        customerRepo.save(customer);

        // ----------------------------------------------------
        // STEP 5 — Prepare roles response
        // ----------------------------------------------------
        Set<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return new RegisterResult(user.getUsername(), roles);
    }

    /**
     * Result returned after successful registration
     */
    public record RegisterResult(String username, Set<String> roles) {}
}