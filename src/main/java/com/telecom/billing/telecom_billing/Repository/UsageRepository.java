package com.telecom.billing.telecom_billing.Repository;


import com.telecom.billing.telecom_billing.Models.Usage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface UsageRepository extends JpaRepository<Usage, Long> {
	// Fetch all usage records for a given customer (by customer_id FK)
	List<Usage> findByCustomerId(Long customerId);
   
	 // Fetch usage for a customer within a specific date range (e.g., monthly usage)
	List<Usage> findByCustomerIdAndUsageDateBetween(Long customerId, LocalDate start, LocalDate end);
}
