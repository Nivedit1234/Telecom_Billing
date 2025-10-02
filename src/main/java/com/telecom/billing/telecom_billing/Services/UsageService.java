package com.telecom.billing.telecom_billing.Services;

import com.telecom.billing.telecom_billing.Models.Usage;

import java.time.LocalDate;
import java.util.List;


public interface UsageService {
    void generateMonthlyUsageForAllCustomers(int recordsPerCustomer);
    List<Usage> getUsageForCustomer(Long customerId);
    List<Usage> getUsageForCustomerBetween(Long customerId, LocalDate start, LocalDate end);
}
