package com.telecom.billing.telecom_billing.Repository;


import com.telecom.billing.telecom_billing.Models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByCustomerId(Long customerId);
    List<Invoice> findByBillingMonth(LocalDate month);
}
