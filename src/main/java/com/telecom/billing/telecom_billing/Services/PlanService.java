package com.telecom.billing.telecom_billing.Services;

import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Repository.PlanRepository;
import org.springframework.stereotype.Service;
import com.telecom.billing.telecom_billing.exception.BadRequestException;
import com.telecom.billing.telecom_billing.exception.ResourceNotFoundException;

import java.util.List;


/**
 * PlanService - Handles business logic related to telecom plans.
 *
 * Responsibilities:
 * 1. Fetch all plans from the database.
 * 2. Retrieve a single plan by ID with proper error handling.
 *
 * Annotations:
 *  @Service
 *      - Marks this class as a Spring service component.
 *      - Enables automatic detection and dependency injection.
 *
 * Constructor Injection:
 *  - Injects PlanRepository using constructor injection (best practice).
 *  - Makes the service immutable and easy to test.
 *
 * Methods:
 *  getAllPlans()
 *      - Returns a list of all available plans using repo.findAll().
 *
 *  getPlanById(int id)
 *      - Looks up a plan using repo.findById(id).
 *      - Throws RuntimeException if the plan does not exist.
 *      - Ensures controllers/services don't receive null values.
 *
 * Purpose:
 *  - Acts as the business layer between controllers and the database.
 *  - Encapsulates all plan-related operations for clarity and reuse.
 */

import java.util.List;

/**
 * PlanService - Handles business logic related to telecom plans.
 *
 * Throws ResourceNotFoundException when a requested plan does not exist.
 */
@Service
public class PlanService {

    private final PlanRepository repo;

    public PlanService(PlanRepository repo) {
        this.repo = repo;
    }

    public List<Plan> getAllPlans() {
        return repo.findAll();
    }

    public Plan getPlanById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));
    }
}

