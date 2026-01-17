package com.telecom.billing.telecom_billing.Services;

import com.telecom.billing.telecom_billing.Controllers.RegisterRequest;
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

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final CustomerRepository customerRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor injection (best practice for testability + immutability)
    public AuthService(UserRepository userRepo,
                       CustomerRepository customerRepo,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 🔹 Handles registration of BOTH:
     *      - User (authentication entity)
     *      - Customer (telecom customer profile)
     *
     * 🔹 Annotated with @Transactional → guarantees ATOMICITY
     *      → If ANY exception occurs in this method, Spring rolls back
     *        ALL DB operations automatically.
     *
     * 🔹 We throw ConflictException (custom RuntimeException) for:
     *      - Duplicate phone numbers
     *      - DB constraint violations
     *      - Persistence failures
     */
    @Transactional
    public RegisterResult register(RegisterRequest req) {

        String phone = req.getPhoneNumber().trim();

        // ----------------------------------------------------
        // STEP 1 — Check if user already exists
        // ----------------------------------------------------
        // This checks the "username" column (the phone number)
        // If true → we abort with 409 Conflict
        if (userRepo.existsByUsername(phone)) {
            throw new ConflictException("User already exists with this phone number");
        }

        // ----------------------------------------------------
        // STEP 2 — Build User entity (authentication account)
        // ----------------------------------------------------
        User u = new User();
        u.setUsername(phone);  // username == phone number
        u.setEmail(req.getEmail() != null ? req.getEmail() : phone + "@example.com");
        u.setPassword(passwordEncoder.encode(req.getPassword())); // encrypted password
        u.getRoles().add(Role.ROLE_USER);
        u.setEnabled(true);

        // ----------------------------------------------------
        // STEP 2A — Save user
        // ----------------------------------------------------
        // ⚠️ IMPORTANT:
        // userRepo.save() may throw:
        //  - DataIntegrityViolationException (unique constraint, FK issues)
        //  - ConstraintViolationException (Hibernate validator)
        //  - PersistenceException
        //
        // These exceptions are *checked* or wrapped in RuntimeExceptions.
        // We catch ANY exception here and rethrow as ConflictException (unchecked)
        // so Spring's @Transactional can trigger rollback.
        try {
            u = userRepo.save(u);
        } catch (Exception ex) {
            // RETHROWING AS UNCHECKED EXCEPTION
            throw new ConflictException("Failed to create user: " + ex.getMessage(), ex);
        }

        // ----------------------------------------------------
        // STEP 3 — Create or fetch Customer entity
        // ----------------------------------------------------
        // Customer is the telecom profile. It may already exist in DB
        // if seeded or created earlier manually by admin.
        Customer customer = customerRepo.findByPhoneNumber(phone).orElse(null);

        if (customer == null) {
            // Create new customer if not found
            customer = new Customer();
            customer.setPhoneNumber(phone);
            customer.setName(req.getName() != null ? req.getName() : "");
            customer.setEmail(req.getEmail());

            // Saving this may also throw DB exceptions → catch + wrap
            try {
                customer = customerRepo.save(customer);
            } catch (Exception ex) {
                // RETHROWING AS UNCHECKED EXCEPTION → triggers rollback
                throw new ConflictException("Failed to create customer: " + ex.getMessage(), ex);
            }
        }

        // ----------------------------------------------------
        // STEP 4 — Link Customer → User (set FK customer.user_id)
        // ----------------------------------------------------
        // This performs:
        //   UPDATE customer SET user_id = ? WHERE id = ?
        // If user_id violates a constraint → exception thrown automatically.
        customer.setUser(u);
        customerRepo.save(customer); // also participates in @Transactional context

        // ----------------------------------------------------
        // STEP 5 — Prepare response containing roles
        // ----------------------------------------------------
        Set<String> roles = u.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        // This result is returned to controller to build LoginResponse
        return new RegisterResult(u.getUsername(), roles);
    }

    /**
     * Response returned to controller after successful registration.
     * This is a simple immutable record containing username + roles.
     */
    public record RegisterResult(String username, Set<String> roles) {}
}
