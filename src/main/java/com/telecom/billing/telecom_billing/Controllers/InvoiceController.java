package com.telecom.billing.telecom_billing.Controllers;

import com.telecom.billing.telecom_billing.Models.Invoice;
import com.telecom.billing.telecom_billing.Services.InvoiceService;
import com.telecom.billing.telecom_billing.exception.BadRequestException;
import com.telecom.billing.telecom_billing.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController  // Marks this class as a REST API controller (returns JSON)
@RequestMapping("/invoices") // Base URL for all invoice-related APIs
public class InvoiceController {

    private final InvoiceService service;

    // Constructor injection → recommended way to inject dependencies
    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    /**
     * Generate an invoice for a given customer.
     *
     * Endpoint: POST /invoices/generateInvoice/{customerId}?month=YYYY-MM
     * - customerId → taken from the URL
     * - month (optional) → billing month (format: YYYY-MM)
     *   If not provided, defaults to the current month.
     */
    @PostMapping("/generateInvoice/{customerId}")
    public Invoice generateInvoice(
            @PathVariable Long customerId,        // Extract customerId from URL
            @RequestParam(required = false) String month // Optional query param
    ) {

        YearMonth ym; // Year + Month object (e.g., 2025-10)

        try {
            // If month is not provided → use current month
            // Else → parse the provided month (must be YYYY-MM)
            ym = (month == null || month.isBlank())
                    ? YearMonth.now()
                    : YearMonth.parse(month.trim());
        } catch (Exception e) {
            // Replace generic IllegalArgumentException → custom BadRequest
            throw new BadRequestException("Invalid month format. Use YYYY-MM, e.g., 2025-10");
        }

        // Service may throw NotFoundException if customer doesn't exist
        Invoice invoice = service.generateMonthlyInvoice(customerId, ym);

        if (invoice == null) {
            throw new NotFoundException("Invoice could not be generated.");
        }

        return invoice;
    }

    /**
     * Get all invoices for a specific customer.
     *
     * Endpoint: GET /invoices/{customerId}
     * Returns: List of Invoice objects in JSON.
     */
    @GetMapping("/{customerId}")
    public List<Invoice> getInvoices(@PathVariable Long customerId) {

        List<Invoice> invoices = service.getInvoicesForCustomer(customerId);

        if (invoices == null || invoices.isEmpty()) {
            throw new NotFoundException("No invoices found for customer ID: " + customerId);
        }

        return invoices;
    }
}
