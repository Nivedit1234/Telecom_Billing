package com.telecom.billing.telecom_billing.Controllers;

import com.telecom.billing.telecom_billing.Services.AdminService;
import com.telecom.billing.telecom_billing.exception.ConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * AdminController - endpoints usable by admins only.
 * Endpoint: POST /admin/customers/create-user
 */
@RestController
@RequestMapping("/admin/customers")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Create a user and customer if missing and link them.
     * Protected by ROLE_ADMIN.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create-user")
    public ResponseEntity<?> createUserForCustomer(@RequestBody CreateUserForCustomerDto dto) {
        try {
            AdminService.AdminResult r = adminService.createUserForCustomer(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AdminResponse(r.username(), r.userId(), r.customerId(), r.roles()));
        } catch (ConflictException ce) {
            // Let your global exception handler or consistency determine response format,
            // but for safety we return 409 here.
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ce.getMessage());
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(iae.getMessage());
        } catch (Exception ex) {
            // Unexpected server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + ex.getMessage());
        }
    }
}
