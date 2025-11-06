package com.gbg.userservice.infrastructure.config.jwt;

import com.gbg.userservice.domain.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @Value("${jwt.expiration}")
    private Duration validity;

    private static final String BEARER_PREFIX = "Bearer ";

    @PostConstruct
    private void getSigningKey() {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(UUID id, String username, UserRole roles) {
        Claims claims = Jwts.claims().setSubject(id.toString());
        claims.put("username", username);
        claims.put("roles", roles.toString());
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validity.toMillis());

        return BEARER_PREFIX + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String getUsername(String token) {
        return parseClaims(token)
            .get("username", String.class);
    }

    public String getRole(String token) {
        return parseClaims(token)
            .get("roles", String.class);
    }

    public UUID getId(String token) {
        return UUID.fromString(parseClaims(token).getSubject());
    }



}