package com.telecom.billing.telecom_billing.Services;

import com.telecom.billing.telecom_billing.Models.Invoice;

import java.time.YearMonth;
import java.util.List;

public interface InvoiceService {
    Invoice generateMonthlyInvoice(Long customerId, YearMonth ym);
    List<Invoice> getInvoicesForCustomer(Long customerId);
}
