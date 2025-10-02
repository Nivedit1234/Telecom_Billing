package com.telecom.billing.telecom_billing.Repository;


import com.telecom.billing.telecom_billing.Models.Usage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface UsageRepository extends JpaRepository<Usage, Long> {
    List<Usage> findByCustomerId(Long customerId);
    List<Usage> findByCustomerIdAndUsageDateBetween(Long customerId, LocalDate start, LocalDate end);
}
