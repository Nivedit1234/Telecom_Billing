package com.telecom.billing.telecom_billing.security;

import com.telecom.billing.telecom_billing.Models.User;
import com.telecom.billing.telecom_billing.Repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    // Constructor-based dependency injection
    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * This method is called by Spring Security during authentication.
     * It loads the user from DB and converts it into Spring Security's UserDetails.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1️⃣ Fetch user from database
        User user = repo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username)
                );

        // 2️⃣ Convert Role enum → Spring Security authorities (ROLE_USER, ROLE_ADMIN, etc.)
        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());

        // 3️⃣ Build Spring Security UserDetails object
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())     // already BCrypt-hashed
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())      // if enabled=false → login blocked
                .build();
    }
}