package com.telecom.billing.telecom_billing.Config;


import com.telecom.billing.telecom_billing.Models.Plan;
import com.telecom.billing.telecom_billing.Repository.PlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner loadDefaultPlans(PlanRepository planRepository) {
        return args -> {
            if (planRepository.count() == 0) {
                planRepository.save(new Plan(null, "POSTPAID100", "Postpaid 100", 0.5, 10.0, 0.2, null));
                planRepository.save(new Plan(null, "POSTPAID200", "Postpaid 200", 0.3, 8.0, 0.15, null));
                planRepository.save(new Plan(null, "UNLIMITED399", "Unlimited 399", 0.0, 0.0, 0.0, null));
                System.out.println("📦 Default plans loaded into database!");
            }
        };
    }
}
