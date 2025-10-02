package com.telecom.billing.telecom_billing.Services.Implementation;



import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Models.Invoice;
import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Models.Usage;
import com.telecom.billing.telecom_billing.Repository.CustomerRepository;
import com.telecom.billing.telecom_billing.Repository.InvoiceRepository;
import com.telecom.billing.telecom_billing.Repository.UsageRepository;
import com.telecom.billing.telecom_billing.Services.InvoiceService;
import com.telecom.billing.telecom_billing.Services.UsageService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final UsageService usageService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              CustomerRepository customerRepository,
                              UsageService usageService) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.usageService = usageService;
    }

    @Override
    public Invoice generateMonthlyInvoice(Long customerId, YearMonth ym) {
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        // 1. Fetch customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));

        // 2. Fetch plan
        Plan plan = customer.getPlan();

        // 3. Fetch usage records for this month
        List<Usage> usageList = usageService.getUsageForCustomerBetween(customerId, start, end);

        // 4. Calculate total amount
        double totalMinutes = usageList.stream().mapToDouble(Usage::getMinutesUsed).sum();
        double totalData = usageList.stream().mapToDouble(Usage::getDataUsedGB).sum();
        double totalSms = usageList.stream().mapToDouble(Usage::getSmsSent).sum();

        double amount = (totalMinutes * plan.getRatePerMinute())
                      + (totalData * plan.getRatePerGB())
                      + (totalSms * plan.getRatePerSMS());

        // 5. Create invoice
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setBillingMonth(start); // store first day of month
        invoice.setAmount(amount);
        invoice.setStatus("GENERATED");

        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> getInvoicesForCustomer(Long customerId) {
        return invoiceRepository.findByCustomerId(customerId);
    }
}
