package com.telecom.billing.telecom_billing.Repository;


import com.telecom.billing.telecom_billing.Models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // Fetch all invoices belonging to a specific customer (matches customer_id FK)
    List<Invoice> findByCustomerId(Long customerId);

    // Fetch invoices for a specific billing month (e.g. invoices of 2025-09)
    List<Invoice> findByBillingMonth(LocalDate month);
}
