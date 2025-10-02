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

    

    public List<Plan> getAllPlans() {
        return repo.findAll();
    }



	public Plan getPlanById(int id) {
		return repo.findById(id)
				.orElseThrow(()->new RuntimeException("Plan not found "+ id));
	}
}
