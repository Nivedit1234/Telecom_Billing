package com.telecom.billing.telecom_billing.Controllers;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Models.User;
import com.telecom.billing.telecom_billing.Repository.CustomerRepository;
import com.telecom.billing.telecom_billing.Repository.UserRepository;
import com.telecom.billing.telecom_billing.Services.AuthService;
import com.telecom.billing.telecom_billing.security.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")   // All endpoints begin with /auth
public class AuthController {

    // ---------------------------
    // Dependencies (Injected)
    // ---------------------------

    private final CustomerRepository customerRepo;  // For checking telecom customers
    private final UserRepository userRepo;          // For authentication user accounts
    private final JwtService jwtService;            // For generating JWT tokens
    private final BCryptPasswordEncoder passwordEncoder; // For password hashing & verification
	private AuthService authService;

    // Constructor-based Dependency Injection (Best practice)
    public AuthController(CustomerRepository customerRepo,
                          UserRepository userRepo,
                          JwtService jwtService,
                          BCryptPasswordEncoder passwordEncoder,
                          AuthService authService) {
        this.customerRepo = customerRepo;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authService=authService;
    }

 // -------------------------------------------------------
 // -------------------------------------------------------
 // LOGIN ENDPOINT
 // -------------------------------------------------------
 @PostMapping("/login")
 public ResponseEntity<?> login(@RequestBody @Valid LoginRequest req) {

     // 1️⃣ Try telecom-style login: find the customer based on phone number
     String identifier = req.getPhoneNumber().trim();

     // Try to find a Customer by phone (normal user flow)
     Customer customer = customerRepo.findByPhoneNumber(identifier).orElse(null);

     User user = null;

     if (customer != null) {
         // If a customer exists, use its linked User
         user = customer.getUser();
         if (user == null) {
             return ResponseEntity.badRequest()
                     .body("Customer not linked to a user account.");
         }
     } else {
         // FALLBACK: no customer found — try authenticating directly against User table
         // This allows admin/system accounts (and other non-customer users) to login.
         user = userRepo.findByUsername(identifier).orElse(null);
         if (user == null) {
             // keep response ambiguous for security
             return ResponseEntity.status(401).body("Invalid credentials.");
         }
     }

     // 3️⃣ Verify password using BCrypt (hashed password check)
     if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
         return ResponseEntity.status(401)
                 .body("Invalid credentials.");
     }

     // 4️⃣ Generate JWT token with username + roles inside token
     Set<String> roles = user.getRoles().stream()
             .map(Enum::name)   // Convert Role enum → string
             .collect(Collectors.toSet());

     String token = jwtService.generateToken(user.getUsername(), roles);

     // 5️⃣ Return success response with token + username + roles
     return ResponseEntity.ok(new LoginResponse(
             token,
             user.getUsername(),
             roles
     ));
 }


    // -------------------------------------------------------
    // REGISTER ENDPOINT
    // -------------------------------------------------------
 @PostMapping("/register")
 public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {

     AuthService.RegisterResult res = authService.register(req);

     String token = jwtService.generateToken(res.username(), res.roles());

     return ResponseEntity.ok(new LoginResponse(token, res.username(), res.roles()));
 }


}
