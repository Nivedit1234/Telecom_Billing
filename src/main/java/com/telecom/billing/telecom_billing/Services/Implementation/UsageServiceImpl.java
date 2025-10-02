package com.telecom.billing.telecom_billing.Services.Implementation;

import java.util.Random;
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
    private final Random random;

    public UsageServiceImpl(UsageRepository usageRepository, CustomerRepository customerRepository) {
        this.usageRepository = usageRepository;
        this.customerRepository = customerRepository;
        this.random=new Random();
    }

    @Override
    public void generateMonthlyUsageForAllCustomers(int days) {
        customerRepository.findAll().forEach(customer -> {
            for (int i = 0; i < days; i++) {
                Usage usage = new Usage();
                usage.setCustomer(customer);

                // Usage for each past day
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
        return usageRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Usage> getUsageForCustomerBetween(Long customerId, LocalDate start, LocalDate end) {
        return usageRepository.findByCustomerIdAndUsageDateBetween(customerId, start, end);
    }


	

}
