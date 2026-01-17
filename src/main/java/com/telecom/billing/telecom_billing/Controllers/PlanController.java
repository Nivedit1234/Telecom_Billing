package com.telecom.billing.telecom_billing.Controllers;

import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Services.PlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a REST controller that returns JSON responses
@RequestMapping("/plans") // All APIs inside this controller start with /plans
public class PlanController {

    // Service layer dependency for plan-related operations
    private final PlanService service;

    // Constructor-based dependency injection (recommended in Spring)
    public PlanController(PlanService service) {
        this.service = service;
    }

    /**
     * ---------------------------------------------------------
     * GET /plans/getAllPlans
     * ---------------------------------------------------------
     * Fetch and return all available telecom plans.
     *
     * Uses PlanService.getAllPlans()
     * This method does NOT need exception handling inside controller,
     * because empty list is a valid response.
     */
    @GetMapping("/getAllPlans")
    public List<Plan> getAllPlans() {
        return service.getAllPlans();
    }

    /**
     * ---------------------------------------------------------
     * GET /plans/{id}
     * ---------------------------------------------------------
     * Fetch a single plan by its ID.
     *
     * Example request:
     *   GET /plans/1
     *
     * Any errors (like Plan not found) are thrown by service layer
     * as ResourceNotFoundException → caught by GlobalExceptionHandler.
     */
    @GetMapping("/{id}")
    public Plan getPlan(@PathVariable int id) {
        return service.getPlanById(id); // Exceptions handled globally
    }
}
