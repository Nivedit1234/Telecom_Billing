package com.telecom.billing.telecom_billing.Config;

import com.telecom.billing.telecom_billing.Models.Customer;
import com.telecom.billing.telecom_billing.Models.Role;
import com.telecom.billing.telecom_billing.Models.User;
import com.telecom.billing.telecom_billing.Repository.CustomerRepository;
import com.telecom.billing.telecom_billing.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class SeedUsersByPhone {

    /**
     * Seeder will run only when there are NO users in the app_user table.
     * This guarantees "run once" semantics for a fresh database.
     */
    @Bean("seedUsersByPhoneOnce")
    @ConditionalOnMissingBean(name = "seedUsersByPhoneOnce")
    public CommandLineRunner seedUsersByPhone(UserRepository userRepo,
                                              CustomerRepository customerRepo) {
        return args -> {
            // If users already exist, skip seeding entirely (ensures run-once behavior)
            long userCount = userRepo.count();
            if (userCount > 0) {
                System.out.println("[SeedUsersByPhone] Skipping seeding because app_user already has " + userCount + " user(s).");
                return;
            }

            final String defaultPassword = "Telco@123"; // dev only — change later
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            List<Customer> customers = customerRepo.findAll();
            if (customers.isEmpty()) {
                System.out.println("[SeedUsersByPhone] No customers found to seed.");
                return;
            }

            for (Customer c : customers) {
                try {
                    // If customer already linked, skip
                    if (c.getUser() != null) {
                        System.out.println("[SeedUsersByPhone] Skipping customer id=" + c.getId() + " (already linked).");
                        continue;
                    }

                    String phone = c.getPhoneNumber();
                    if (phone == null || phone.isBlank()) {
                        System.out.println("[SeedUsersByPhone] Skipping customer id=" + c.getId() + " (no phone)");
                        continue;
                    }

                    String username = phone;
                    if (userRepo.existsByUsername(username)) {
                        username = phone + "_" + c.getId();
                    }

                    User u = new User();
                    u.setUsername(username);
                    u.setEmail(c.getEmail() != null ? c.getEmail() : username + "@example.com");
                    u.setPassword(encoder.encode(defaultPassword));
                    u.getRoles().add(Role.ROLE_USER);
                    u.setEnabled(true);

                    u = userRepo.save(u);

                    c.setUser(u);
                    customerRepo.save(c);

                    System.out.println("[SeedUsersByPhone] Created user for customerId=" + c.getId()
                            + " -> username=" + u.getUsername() + " password=" + defaultPassword);
                } catch (Exception ex) {
                    System.err.println("[SeedUsersByPhone] Failed for customer id=" + c.getId() + " : " + ex.getMessage());
                }
            }
            System.out.println("[SeedUsersByPhone] Seeding complete.");
        };
    }
}
