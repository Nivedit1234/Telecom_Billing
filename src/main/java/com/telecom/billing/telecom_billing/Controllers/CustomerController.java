package com.telecom.billing.telecom_billing.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Models.User;
import com.telecom.billing.telecom_billing.Repository.CustomerRepository;
import com.telecom.billing.telecom_billing.Repository.UserRepository;
import com.telecom.billing.telecom_billing.Services.CustomerService;
import com.telecom.billing.telecom_billing.exception.ConflictException;
import com.telecom.billing.telecom_billing.exception.NotFoundException;

import jakarta.validation.Valid;

/**
 * Controller for customer-related endpoints.
 *
 * - /customers/getAll    (ADMIN only)   -> list all customers
 * - /customers/create    (ADMIN only)   -> create a new customer
 * - /customers/me        (AUTHENTICATED)-> return customer profile for current user
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

	private final CustomerService customerService;
	private final CustomerRepository customerRepo;
    private final UserRepository userRepo;

    public CustomerController(CustomerService customerService, UserRepository userRepo,CustomerRepository customerRepo) {
        this.customerService= customerService;
        this.userRepo = userRepo;
        this.customerRepo=customerRepo;
    }

    // ---------------------------
    // Admin-only list
    // ---------------------------
    // @PreAuthorize ensures only users with ROLE_ADMIN can call this endpoint.
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();   // ✅ goes through cache
    }

    // ---------------------------
    // Admin-only create
    // ---------------------------
    // Accepts CreateCustomerDto (validated). Returns 201 CREATED with created customer.
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createCustomer(@RequestBody @Valid CreateCustomerDto dto) {
        // check duplicate by phone (unique constraint)
        Optional<Customer> existing = customerRepo.findByPhoneNumber(dto.getPhoneNumber());
        if (existing.isPresent()) {
            // return conflict via exception -> GlobalExceptionHandler will convert to JSON 409
            throw new ConflictException("Customer already exists with phone: " + dto.getPhoneNumber());
        }

        Customer c = new Customer();
        c.setName(dto.getName());
        c.setPhoneNumber(dto.getPhoneNumber());
        c.setEmail(dto.getEmail());
        // set plan if dto provided (left as exercise)

        try {
            Customer saved = customerRepo.save(c);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception ex) {
            // translate persistence errors to ConflictException so it's consistent
            throw new ConflictException("Failed to create customer: " + ex.getMessage(), ex);
        }
    }

    // ---------------------------
    // Current logged-in user's customer profile
    // ---------------------------
    // Returns the customer profile linked to the authenticated user.
    @GetMapping("/me")
    public ResponseEntity<?> getMyCustomer(Authentication authentication) {
        // If not authenticated, return 401 (keep this as ResponseEntity to avoid filter behaviour)
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Determine username from Authentication principal
        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = String.valueOf(principal);
        }

        // Find the User entity; throw NotFoundException -> handled by GlobalExceptionHandler
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));

        // Find Customer by user (customerRepo.findByUser should be implemented)
        Customer customer = customerRepo.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Customer profile not found for user: " + username));

        return ResponseEntity.ok(customer);
    }
}