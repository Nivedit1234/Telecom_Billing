package com.telecom.billing.telecom_billing.Controllers;

import com.telecom.billing.telecom_billing.Models.Usage;
import com.telecom.billing.telecom_billing.Services.UsageService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/usage")
public class UsageController {

    private final UsageService usageService;

    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }

    // ---------------------------------------------------------
    // Get all usage records for a customer
    // ---------------------------------------------------------
    // Example: GET /usage/getAllUsage/1
    @GetMapping("getAllUsage/{customerId}")
    public List<Usage> getCustomerUsage(@PathVariable Long customerId) {
        // Basic sanity check — invalid IDs are considered bad requests
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Invalid customerId provided");
        }
        // Delegate to service; any not-found or DB errors should be thrown
        // by service layer and handled by the global exception handler.
        return usageService.getUsageForCustomer(customerId);
    }

    // ---------------------------------------------------------
    // Get usage for a customer within a date range (for invoice calculation)
    // ---------------------------------------------------------
    // Example: GET /usage/{customerId}/between?startDate=2025-10-01&endDate=2025-10-31
    @GetMapping("/{customerId}/between")
    public List<Usage> getCustomerUsageBetween(
            @PathVariable Long customerId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        // Input validation
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Invalid customerId provided");
        }
        if (startDate == null || startDate.isBlank() || endDate == null || endDate.isBlank()) {
            throw new IllegalArgumentException("startDate and endDate are required (format: YYYY-MM-DD)");
        }

        LocalDate start;
        LocalDate end;
        try {
            start = LocalDate.parse(startDate.trim());
            end = LocalDate.parse(endDate.trim());
        } catch (DateTimeParseException ex) {
            // Let global handler convert this into a 400 with a helpful message
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD, e.g. 2025-10-01");
        }

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("endDate must be the same as or after startDate");
        }

        return usageService.getUsageForCustomerBetween(customerId, start, end);
    }

}
