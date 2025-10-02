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

	// ✅ Explicitly register a customer
	@PostMapping("/register/{planId}")
	public Customer registerCustomer(@PathVariable Long planId, @RequestBody Customer customer) {
		return service.registerCustomer(customer, planId);
	}

	// ✅ Explicitly fetch all customers
	@GetMapping("/getAll")
	public List<Customer> getAllCustomers() {
		return service.getAllCustomers();
	}

	// ✅ Explicitly fetch customer by ID
	@GetMapping("/getById/{id}")
	public Customer getCustomerById(@PathVariable Long id) {
		return service.getCustomerById(id);
	}
}
