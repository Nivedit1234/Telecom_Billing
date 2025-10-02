package com.telecom.billing.telecom_billing.Services.Implementation;


import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Models.Usage;
import com.telecom.billing.telecom_billing.Repository.CustomerRepository;
import com.telecom.billing.telecom_billing.Repository.UsageRepository;
import com.telecom.billing.telecom_billing.Services.UsageService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UsageServiceImpl implements UsageService {

    private final UsageRepository usageRepository;
    private final CustomerRepository customerRepository;

    public UsageServiceImpl(UsageRepository usageRepository, CustomerRepository customerRepository) {
        this.usageRepository = usageRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Usage generateRandomUsage(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Usage usage = new Usage();
        usage.setCustomer(customer);
        usage.setUsageDate(LocalDate.now());

        // Simulated random usage
        usage.setMinutesUsed((int) (Math.random() * 200));  // up to 200 mins
        usage.setMinutesUsed((int) (Math.random() * 50));       // up to 50 SMS
        usage.setDataUsedGB((int) (Math.random() * 2048));  // up to 2GB

        return usageRepository.save(usage);
    }

    @Override
    public List<Usage> getUsageForCustomer(Long customerId) {
        return usageRepository.findByCustomerId(customerId);
    }
}
