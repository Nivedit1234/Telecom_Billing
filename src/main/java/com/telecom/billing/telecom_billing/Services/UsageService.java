package com.telecom.billing.telecom_billing.Services;

import com.telecom.billing.telecom_billing.Models.Usage;
import java.util.List;


public interface UsageService {
    Usage generateRandomUsage(Long customerId);
    List<Usage> getUsageForCustomer(Long customerId);
}
