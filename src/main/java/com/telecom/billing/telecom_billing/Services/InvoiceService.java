package com.telecom.billing.telecom_billing.Services;

import com.telecom.billing.telecom_billing.Models.Invoice;

import java.time.YearMonth;
import java.util.List;

public interface InvoiceService {

    // Generate monthly invoice for a given customer and month
    Invoice generateMonthlyInvoice(Long customerId, YearMonth ym);

    // Fetch all invoices for a customer
    List<Invoice> getInvoicesForCustomer(Long customerId);
}
