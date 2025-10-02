package com.telecom.billing.telecom_billing.Controllers;


import com.telecom.billing.telecom_billing.Models.Usage;
import com.telecom.billing.telecom_billing.Services.UsageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usage")
public class UsageController {

    private final UsageService usageService;

    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }

    // Generate random usage for a customer
    @PostMapping("/generate/{customerId}")
    public Usage generateUsage(@PathVariable Long customerId) {
        return usageService.generateRandomUsage(customerId);
    }

    // Get all usage records for a customer
    @GetMapping("/{customerId}")
    public List<Usage> getCustomerUsage(@PathVariable Long customerId) {
        return usageService.getUsageForCustomer(customerId);
    }
}
