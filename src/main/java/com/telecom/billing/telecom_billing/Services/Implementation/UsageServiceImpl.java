package com.telecom.billing.telecom_billing.Services.Implementation;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.telecom.billing.telecom_billing.Models.Usage;
import com.telecom.billing.telecom_billing.Repository.CustomerRepository;
import com.telecom.billing.telecom_billing.Repository.UsageRepository;
import com.telecom.billing.telecom_billing.Services.UsageService;
import com.telecom.billing.telecom_billing.exception.ResourceNotFoundException;



/**
 * UsageServiceImpl
 *
 * Implements the UsageService interface and contains all business logic
 * related to telecom usage generation and retrieval.
 *
 * Responsibilities:
 * ----------------------------------------------------
 * 1. Generate synthetic usage data for all customers
 *    - Used only in development/testing.
 *    - Creates realistic random usage for calls, SMS, and data.
 *    - Helps simulate monthly usage to test invoice generation.
 *
 * 2. Fetch complete usage history for a given customer
 *    - Returns all usage records stored in the database.
 *    - Useful for customer dashboards and admin analytics.
 *
 * 3. Fetch usage for a customer within a date range
 *    - Critical for monthly invoice generation.
 *    - InvoiceService calls this to get usage for the billing cycle.
 *
 * How it works:
 * ----------------------------------------------------
 * - Uses CustomerRepository to fetch customers when generating usage.
 * - Uses UsageRepository to save or retrieve usage entries.
 * - Random class generates realistic usage values for testing.
 * - Each usage entry includes call minutes, SMS count, and data usage.
 *
 * When used:
 * ----------------------------------------------------
 * - generateMonthlyUsageForAllCustomers() is used only in dev mode
 *   or manually triggered to generate dummy data.
 * - getUsageForCustomer() and getUsageForCustomerBetween()
 *   are used during invoice generation and for usage history APIs.
 *
 * Notes:
 * ----------------------------------------------------
 * - This class does not handle billing; it only provides usage data.
 * - InvoiceService consumes this service to calculate monthly bills.
 * - Production systems would receive real usage from telecom systems,
 *   not generated randomly as done here.
 */




@Service
public class UsageServiceImpl implements UsageService {

    private final UsageRepository usageRepository;
    private final CustomerRepository customerRepository;
    private final Random random;

    public UsageServiceImpl(UsageRepository usageRepository, CustomerRepository customerRepository) {
        this.usageRepository = usageRepository;
        this.customerRepository = customerRepository;
        this.random = new Random();
    }

    @Override
    public void generateMonthlyUsageForAllCustomers(int days) {
        customerRepository.findAll().forEach(customer -> {
            for (int i = 0; i < days; i++) {
                Usage usage = new Usage();
                usage.setCustomer(customer);

                // Usage date per past day
                usage.setUsageDate(LocalDate.now().minusDays(i));

                // Random realistic usage
                usage.setMinutesUsed(random.nextInt(60)); // up to 1 hr/day
                usage.setDataUsedGB(Math.round(random.nextDouble() * 2 * 100.0) / 100.0); // up to 2 GB/day
                usage.setSmsSent(random.nextInt(20)); // up to 20 SMS/day

                usageRepository.save(usage);
            }
        });
    }

    @Override
    public List<Usage> getUsageForCustomer(Long customerId) {
        // Validate customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found: " + customerId);
        }
        return usageRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Usage> getUsageForCustomerBetween(Long customerId, LocalDate start, LocalDate end) {
        // Validate customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found: " + customerId);
        }
        return usageRepository.findByCustomerIdAndUsageDateBetween(customerId, start, end);
    }
}
