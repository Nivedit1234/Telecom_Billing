package com.telecom.billing.telecom_billing.Repository;


import com.telecom.billing.telecom_billing.Models.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findById(int id);
}
