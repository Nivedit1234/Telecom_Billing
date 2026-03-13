package com.telecom.billing.telecom_billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
//activates Spring 7caching mechanism.
public class TelecomBillingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelecomBillingApplication.class, args);
	}

	
}