package com.telecom.billing.telecom_billing.Services;


import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Repository.CustomerRepository;
import com.telecom.billing.telecom_billing.Repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepo;
    private final PlanRepository planRepo;

    public CustomerService(CustomerRepository customerRepo, PlanRepository planRepo) {
        this.customerRepo = customerRepo;
        this.planRepo = planRepo;
    }

    public Customer registerCustomer(Customer customer, Long planId) {
        Plan plan = planRepo.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));
        System.out.println(plan);
        customer.setPlan(plan);
        return customerRepo.save(customer);
    }


    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}

