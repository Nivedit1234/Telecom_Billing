package com.telecom.billing.telecom_billing.Controllers;

import com.telecom.billing.telecom_billing.Models.Usage;
import com.telecom.billing.telecom_billing.Services.UsageService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usage")
public class UsageController {

    private final UsageService usageService;

    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }

    // Get all usage records for a customer
    @GetMapping("getAllUsage/{customerId}")
    public List<Usage> getCustomerUsage(@PathVariable Long customerId) {
        return usageService.getUsageForCustomer(customerId);
    }

    // Get usage for a customer within a date range (for invoice calculation)
    @GetMapping("/{customerId}/between")
    public List<Usage> getCustomerUsageBetween(
            @PathVariable Long customerId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate.trim());
        LocalDate end = LocalDate.parse(endDate.trim());
        return usageService.getUsageForCustomerBetween(customerId, start, end);
    }

}
