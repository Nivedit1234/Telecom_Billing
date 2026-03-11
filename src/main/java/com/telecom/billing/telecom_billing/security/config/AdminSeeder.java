package com.telecom.billing.telecom_billing.security.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.telecom.billing.telecom_billing.Models.Role;
import com.telecom.billing.telecom_billing.Models.User;
import com.telecom.billing.telecom_billing.Repository.UserRepository;

@Configuration
public class AdminSeeder {

    @Bean
    public CommandLineRunner createAdmin(UserRepository userRepo) {
        return args -> {

            // check if admin exists already
            if (userRepo.existsByUsername("admin")) {
                System.out.println("[AdminSeeder] Admin already exists. Skipping seeding.");
                return;
            }

            System.out.println("[AdminSeeder] Creating default ADMIN user...");

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@telco.com");
            admin.setPassword(encoder.encode("Admin@123")); // strong password
            admin.getRoles().add(Role.ROLE_ADMIN);
            admin.setEnabled(true);

            userRepo.save(admin);

            System.out.println("[AdminSeeder] Admin created successfully!");
        };
    }
}
