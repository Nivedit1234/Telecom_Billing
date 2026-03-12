package com.telecom.billing.telecom_billing.Services;

import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Repository.PlanRepository;
import com.telecom.billing.telecom_billing.exception.ResourceNotFoundException;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PlanService - Handles business logic related to telecom plans.
 *
 * Responsibilities:
 * 1. Fetch all plans from the database.
 * 2. Retrieve a single plan by ID with proper error handling.
 * 3. Integrate Redis caching to reduce repeated database queries.
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
 *      - Returns a list of all available telecom plans using repo.findAll().
 *      - Uses Redis caching via @Cacheable("plans").
 *      - First request → DB call → result cached in Redis.
 *      - Subsequent requests → served directly from Redis.
 *
 *  getPlanById(Long id)
 *      - Looks up a plan using repo.findById(id).
 *      - Throws ResourceNotFoundException if the plan does not exist.
 *      - Uses Redis caching via @Cacheable(value="plan", key="#id").
 *      - Prevents repeated database queries for frequently accessed plans.
 *
 * Purpose:
 *  - Acts as the business layer between controllers and the database.
 *  - Encapsulates all plan-related operations for clarity and reuse.
 *  - Improves performance using Redis caching for read-heavy APIs.
 *
 * Redis Caching Flow:
 *
 *   Client Request
 *        ↓
 *   PlanController
 *        ↓
 *   PlanService
 *        ↓
 *   Redis Cache Check
 *        ↓
 *   Cache Miss → Database Query → Store in Redis → Return Response
 *   Cache Hit  → Return Data Directly from Redis
 */

@Service
public class PlanService {

    /**
     * Repository dependency responsible for database operations
     * related to Plan entities.
     */
    private final PlanRepository repo;

    /**
     * Constructor-based dependency injection.
     * Spring automatically injects the PlanRepository bean here.
     */
    public PlanService(PlanRepository repo) {
        this.repo = repo;
    }

    /**
     * ---------------------------------------------------------
     * Fetch all telecom plans
     * ---------------------------------------------------------
     *
     * Annotation:
     *  @Cacheable("plans")
     *
     * Behavior:
     *  - Checks Redis cache for stored plans.
     *  - If present → returns cached data.
     *  - If absent → fetches from database and stores result in Redis.
     *
     * Example Redis Key:
     *      plans::SimpleKey[]
     *
     * Returns:
     *  - List of all available telecom plans.
     */
    @Cacheable("plans")
    public List<Plan> getAllPlans() {
        System.out.println("DB HIT");
    	return repo.findAll();
    }

    /**
     * ---------------------------------------------------------
     * Fetch a telecom plan by ID
     * ---------------------------------------------------------
     *
     * Annotation:
     *  @Cacheable(value = "plan", key = "#id")
     *
     * Behavior:
     *  - Checks Redis cache using the plan ID as the key.
     *  - If the plan exists in Redis → returns cached object.
     *  - Otherwise → queries database and stores result in Redis.
     *
     * Example Redis Key:
     *      plan::1
     *
     * Parameters:
     *  id → Unique identifier of the telecom plan.
     *
     * Error Handling:
     *  - Throws ResourceNotFoundException if the plan does not exist.
     *
     * Returns:
     *  - Plan object corresponding to the given ID.
     */
    @Cacheable(value = "plan", key = "#id")
    public Plan getPlanById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));
    }
}