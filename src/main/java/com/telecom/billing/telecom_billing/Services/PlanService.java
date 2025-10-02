package com.telecom.billing.telecom_billing.Services;

import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    private final PlanRepository repo;

    public PlanService(PlanRepository repo) {
        this.repo = repo;
    }

    public Plan createPlan(Plan plan) {
        return repo.save(plan);
    }

    public List<Plan> getAllPlans() {
        return repo.findAll();
    }

    public Plan getPlanByCode(String code) {
        return repo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Plan not found: " + code));
    }
}
