package com.telecom.billing.telecom_billing.Services;

import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Repository.CustomerRepository;
import com.telecom.billing.telecom_billing.Repository.PlanRepository;
import com.telecom.billing.telecom_billing.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Marks this class as a Spring service (business logic layer)
public class CustomerService {

    private final CustomerRepository customerRepo;
    private final PlanRepository planRepo;

    public CustomerService(CustomerRepository customerRepo, PlanRepository planRepo) {
        this.customerRepo = customerRepo;
        this.planRepo = planRepo;
    }

    /**
     * Register a new customer and link to a plan
     * Throws ResourceNotFoundException if plan not found.
     */
    public Customer registerCustomer(Customer customer, Long planId) {
        Plan plan = planRepo.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + planId));
        customer.setPlan(plan);
        return customerRepo.save(customer);
    }

    // Fetch all customers from database
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    // Fetch a customer by ID (throws 404 if not found)
    public Customer getCustomerById(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }
}
