package com.telecom.billing.telecom_billing.Services.Implementation;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Models.Invoice;
import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Models.Usage;
import com.telecom.billing.telecom_billing.Repository.CustomerRepository;
import com.telecom.billing.telecom_billing.Repository.InvoiceRepository;
import com.telecom.billing.telecom_billing.Services.InvoiceService;
import com.telecom.billing.telecom_billing.Services.UsageService;
import com.telecom.billing.telecom_billing.exception.ResourceNotFoundException;




/**
 * InvoiceServiceImpl
 * -------------------
 * This class contains the core business logic for generating invoices and
 * fetching invoice history for customers.
 *
 * Annotations:
 * - @Service: Marks this class as a Spring-managed service bean.
 *
 * Dependencies (injected using constructor injection):
 * - InvoiceRepository: To save and fetch invoices from DB.
 * - CustomerRepository: To load customer and their plan information.
 * - UsageService: To fetch customer usage for a specific month.
 * - UsageRepository: Used for usage DB operations (optional).
 *
 * Methods:
 * 1. generateMonthlyInvoice(Long customerId, YearMonth yearMonth)
 *      - Converts YearMonth into start/end LocalDate
 *      - Fetches customer and their plan
 *      - Fetches usage for that month
 *      - Calculates total minutes, data, and SMS usage
 *      - Computes invoice amount based on plan's pricing
 *      - Creates and saves an invoice record
 *
 * 2. getInvoicesForCustomer(Long customerId)
 *      - Returns all invoices associated with a specific customer
 *
 * Purpose:
 * - Central place for all invoice-related business logic.
 * - Keeps controller clean by isolating logic here.
 * - Allows easy unit testing by mocking repositories.
 */





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

    /**
     * Calculate and persist a monthly invoice for a customer.
     * - Validates customer exists and has a plan (404 if not).
     * - Fetches usage via UsageService (between start and end of month).
     * - Calculates amount and stores invoice.
     */
    @Override
    public Invoice generateMonthlyInvoice(Long customerId, YearMonth ym) {
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        // 1. Fetch customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));

        // 2. Fetch plan
        Plan plan = customer.getPlan();
        if (plan == null) {
            throw new ResourceNotFoundException("Customer does not have a plan assigned.");
        }

        // 3. Fetch usage records for this month via UsageService
        List<Usage> usageList = usageService.getUsageForCustomerBetween(customerId, start, end);

        // 4. Calculate totals (names depend on your Usage/Plan model fields)
        double totalMinutes = usageList.stream().mapToDouble(Usage::getMinutesUsed).sum();
        double totalDataGB = usageList.stream().mapToDouble(Usage::getDataUsedGB).sum();
        double totalSms = usageList.stream().mapToDouble(Usage::getSmsSent).sum();

        double amount = (totalMinutes * plan.getRatePerMinute())
                      + (totalDataGB * plan.getRatePerGB())
                      + (totalSms * plan.getRatePerSMS());

        amount = Math.round(amount * 100.0) / 100.0; // round to 2 decimals

        // 5. Create & save invoice
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setBillingMonth(start); // store first day of month
        invoice.setAmount(amount);
        invoice.setStatus("GENERATED");

        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> getInvoicesForCustomer(Long customerId) {
        // optional: validate customer exists first
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found: " + customerId);
        }
        return invoiceRepository.findByCustomerId(customerId);
    }
}

