package com.telecom.billing.telecom_billing.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This filter runs ONCE per request and attempts to authenticate users
 * based on the JWT sent in the Authorization header.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructor injection of JwtService and UserDetailsService.
     * JwtService → validates & extracts data from token
     * CustomUserDetailsService → loads user info from DB for authentication context
     */
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Core filter logic. Runs before every controller request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        // Extract the Authorization header ("Bearer <token>")
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // If no header or header does not start with Bearer → skip JWT auth
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // stop further JWT processing
        }

        // Extract actual token by removing "Bearer "
        String token = header.substring(7);

        // Validate the JWT (signature, expiration)
        if (!jwtService.validateToken(token)) {
            filterChain.doFilter(request, response);
            return; // invalid token → continue as unauthenticated
        }

        // Extract username from token
        String username = jwtService.extractUsername(token);
        if (username == null) {
            filterChain.doFilter(request, response);
            return; // username missing → skip
        }

        /**
         * Load full user details from database.
         * Needed because Spring Security requires:
         * - Password (not used here)
         * - Authorities (roles/permissions)
         * - Account status flags
         */
        var userDetails = userDetailsService.loadUserByUsername(username);

        // Extract roles from token and convert them to GrantedAuthorities
        Set<SimpleGrantedAuthority> authorities =
                jwtService.extractRoles(token).stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());

        /**
         * Create an Authentication object recognized by Spring Security.
         *
         * UsernamePasswordAuthenticationToken:
         * - principal = userDetails
         * - credentials = null (JWT already verified)
         * - authorities = roles extracted from token
         */
        var auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                authorities
        );

        // Store authentication in Spring's SecurityContext (marks user as logged in)
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Continue filter chain (move to next filter or controller)
        filterChain.doFilter(request, response);
    }
}
