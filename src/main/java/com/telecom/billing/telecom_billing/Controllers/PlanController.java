package com.telecom.billing.telecom_billing.Controllers;

import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Services.PlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PlanController - Exposes REST APIs related to telecom plans.
 *
 * Responsibilities:
 * 1. Allow clients to retrieve all available telecom plans.
 * 2. Allow clients to retrieve a specific plan using its ID.
 *
 * Annotations:
 *  @RestController
 *      - Marks this class as a REST controller.
 *      - Automatically converts returned objects to JSON.
 *
 *  @RequestMapping("/plans")
 *      - Defines the base URL for all endpoints in this controller.
 *      - All APIs inside this class will start with /plans.
 *
 * Constructor Injection:
 *  - Injects PlanService using constructor injection (best practice).
 *  - Promotes immutability and easier unit testing.
 *
 * Methods:
 *  getAllPlans()
 *      - Returns a list of all available telecom plans.
 *      - Internally calls PlanService.getAllPlans().
 *
 *  getPlan(Long id)
 *      - Retrieves a specific plan using its unique ID.
 *      - Delegates the logic to PlanService.getPlanById(id).
 *      - If the plan does not exist, ResourceNotFoundException is thrown.
 *
 * Purpose:
 *  - Acts as the API layer between clients and the business logic layer.
 *  - Delegates business operations to PlanService.
 *  - Keeps controllers thin and focused on request/response handling.
 */

@RestController
@RequestMapping("/plans")
public class PlanController {

    /**
     * Service layer dependency for plan-related operations.
     * All business logic is handled in PlanService.
     */
    private final PlanService service;

    /**
     * Constructor-based dependency injection.
     *
     * Spring automatically injects the PlanService bean here.
     */
    public PlanController(PlanService service) {
        this.service = service;
    }

    /**
     * ---------------------------------------------------------
     * GET /plans
     * ---------------------------------------------------------
     * Fetch and return all available telecom plans.
     *
     * Example Request:
     *      GET /plans
     *
     * Example Response:
     *      [
     *          { "id":1, "name":"Basic Plan", "price":199 },
     *          { "id":2, "name":"Unlimited Plan", "price":499 }
     *      ]
     *
     * Returns:
     *      List of all telecom plans.
     */
    @GetMapping
    public List<Plan> getAllPlans() {
        return service.getAllPlans();
    }

    /**
     * ---------------------------------------------------------
     * GET /plans/{id}
     * ---------------------------------------------------------
     * Fetch a telecom plan using its ID.
     *
     * Example Request:
     *      GET /plans/1
     *
     * Example Response:
     *      { "id":1, "name":"Basic Plan", "price":199 }
     *
     * Error Handling:
     *      If the plan does not exist,
     *      ResourceNotFoundException is thrown in the service layer
     *      and handled by the GlobalExceptionHandler.
     *
     * Parameters:
     *      id → Unique identifier of the telecom plan.
     *
     * Returns:
     *      Plan object corresponding to the provided ID.
     */
    @GetMapping("/{id}")
    public Plan getPlan(@PathVariable Long id) {
        return service.getPlanById(id);
    }
}