package com.telecom.billing.telecom_billing.Services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Repository.CustomerRepository;
import com.telecom.billing.telecom_billing.Repository.PlanRepository;
import com.telecom.billing.telecom_billing.exception.ResourceNotFoundException;

@Service
public class CustomerService {

    private final CustomerRepository customerRepo;
    private final PlanRepository planRepo;

    public CustomerService(CustomerRepository customerRepo, PlanRepository planRepo) {
        this.customerRepo = customerRepo;
        this.planRepo = planRepo;
    }

    /**
     * Register a new customer and link to a plan
     */
    /**@CachePut Used for create/update operations 
     * Save to DB → update Redis cache**/
    @CachePut(value = "customers", key = "#result.id")
    public Customer registerCustomer(Customer customer, Long planId) {

        Plan plan = planRepo.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + planId));

        customer.setPlan(plan);

        return customerRepo.save(customer);
    }

    // Fetch all customers
    //@Cacheable Used for read operations
    //Request → Check Redis    → If exists → return from Redis
   // → If not → query DB → store in Redis → return
    
    @Cacheable(value = "customers")
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    // Fetch customer by ID
    @Cacheable(value = "customers", key = "#id")
    public Customer getCustomerById(Long id) {

        return customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }
}