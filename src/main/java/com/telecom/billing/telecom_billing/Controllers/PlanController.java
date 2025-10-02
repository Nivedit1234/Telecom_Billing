package com.telecom.billing.telecom_billing.Controllers;


import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Services.PlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {

    private final PlanService service;

    public PlanController(PlanService service) {
        this.service = service;
    }


    @GetMapping
    public List<Plan> getAllPlans() {
        return service.getAllPlans();
    }

    @GetMapping("/{code}")
    public Plan getPlan(@PathVariable String code) {
        return service.getPlanByCode(code);
    }
}
