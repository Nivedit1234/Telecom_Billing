package com.telecom.billing.telecom_billing.Services;


import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Models.Invoice;
import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Models.Usage;
import com.telecom.billing.telecom_billing.Repository.CustomerRepository;
import com.telecom.billing.telecom_billing.Repository.InvoiceRepository;
import com.telecom.billing.telecom_billing.Repository.UsageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepo;
    private final CustomerRepository customerRepo;
    private final UsageRepository usageRepo;

    public InvoiceService(InvoiceRepository invoiceRepo, CustomerRepository customerRepo, UsageRepository usageRepo) {
        this.invoiceRepo = invoiceRepo;
        this.customerRepo = customerRepo;
        this.usageRepo = usageRepo;
    }

    public Invoice generateMonthlyInvoice(Long customerId, YearMonth month) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Plan plan = customer.getPlan();

        // First and last day of billing month
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        List<Usage> usageRecords = usageRepo.findByCustomerIdAndUsageDateBetween(customerId, start, end);

        int totalMinutes = usageRecords.stream().mapToInt(Usage::getMinutesUsed).sum();
        double totalData = usageRecords.stream().mapToDouble(Usage::getDataUsedGB).sum();
        int totalSMS = usageRecords.stream().mapToInt(Usage::getSmsSent).sum();

        double amount = (totalMinutes * plan.getRatePerMinute())
                + (totalData * plan.getRatePerGB())
                + (totalSMS * plan.getRatePerSMS());

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setBillingMonth(start);
        invoice.setAmount(amount);
        invoice.setStatus("GENERATED");

        return invoiceRepo.save(invoice);
    }

    public List<Invoice> getInvoicesForCustomer(Long customerId) {
        return invoiceRepo.findByCustomerId(customerId);
    }
}
