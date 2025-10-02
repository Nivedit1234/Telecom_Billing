package com.telecom.billing.telecom_billing.Controllers;


import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Services.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping("/{planCode}")
    public Customer registerCustomer(@PathVariable String planCode, @RequestBody Customer customer) {
        return service.registerCustomer(customer, planCode);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return service.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return service.getCustomerById(id);
    }
}
