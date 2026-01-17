package com.telecom.billing.telecom_billing.security.config;

import com.telecom.billing.telecom_billing.security.CustomUserDetailsService;
import com.telecom.billing.telecom_billing.security.JwtAuthenticationFilter;
import com.telecom.billing.telecom_billing.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration                           // Marks this as a Spring configuration class
@EnableMethodSecurity                    // Enables @PreAuthorize, @Secured on methods
public class SecurityConfig {

    private final JwtService jwtService;                       // Used to validate/parse JWT tokens
    private final CustomUserDetailsService userDetailsService; // Loads users from DB

    // Constructor injection (recommended)
    public SecurityConfig(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // Provides AuthenticationManager for login authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Password encoder bean (BCrypt)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Main Spring Security configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Our custom JWT filter (validates token on each request)
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);

        http
            // Disable CSRF (not needed for APIs using JWT)
            .csrf(AbstractHttpConfigurer::disable)

            // Make Spring Security stateless (no sessions)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Define which URLs need authentication
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/auth/register")
                .permitAll()                           // These endpoints are public
                .anyRequest().authenticated()          // Everything else requires a valid JWT
            )

            // Tell Spring how to load users from DB
            .userDetailsService(userDetailsService)

            // Add JWT filter before Spring's own authentication filter
            .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        // Allow H2 console frames to open in browser
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();  // Build final security configuration
    }
}
