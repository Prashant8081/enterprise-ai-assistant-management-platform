package com.enterpriseai.platform.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import com.enterpriseai.platform.user.UserAccount;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMinutes;

    public JwtService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.expiration-minutes:240}") long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public String generate(UserAccount user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expirationMinutes * 60);
        return Jwts.builder()
            .subject(user.getEmail())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .claims(Map.of(
                "tenantId", user.getTenant().getId().toString(),
                "tenantSlug", user.getTenant().getSlug(),
                "roles", user.getRoles().stream().map(Role::name).toList()))
            .signWith(key)
            .compact();
    }

    public String subject(String token) {
        return claims(token).getSubject();
    }

    public boolean isValid(String token, UserAccount user) {
        return user.getEmail().equalsIgnoreCase(subject(token)) && claims(token).getExpiration().after(new Date());
    }

    private Claims claims(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
