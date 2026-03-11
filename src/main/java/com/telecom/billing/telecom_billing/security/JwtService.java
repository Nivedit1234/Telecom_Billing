package com.telecom.billing.telecom_billing.security;

import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Set;
@Service
public class JwtService {

//    // DEV: change to env property in production
    private final Key key = Keys.hmacShaKeyFor(
            "replace_this_with_a_long_strong_secret_at_least_256_bits_long_replace_me".getBytes()
    );

    private final long jwtExpirationMs = 24 * 60 * 60 * 1000L; // 24 hours

//    private final Key key;
//    private final long jwtExpirationMs;
//
//    public JwtService(
//    		@Value("${jwt.secret}") String secret,
//            @Value("${jwt.expiration}") long jwtExpirationMs
//    ) {
//        this.key = Keys.hmacShaKeyFor(secret.getBytes());
//        this.jwtExpirationMs = jwtExpirationMs;
//    }

    public String generateToken(String username, Set<String> roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);
        String rolesString = roles.stream().collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", rolesString)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Set<String> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        String roles = claims.get("roles", String.class);
        if (roles == null || roles.isBlank()) return Set.of();
        return Set.of(roles.split(","));
    }
}
