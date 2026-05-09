package com.sayan.article_platform.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private final SecretKey key = Keys.hmacShaKeyFor("my-secret-key-my-secret-key-123456".getBytes());

    public String generate(UUID userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new Date(System.currentTimeMillis() + 900000))
                .signWith(key)
                .compact();
    }

    public UUID extract(String token) {
        return UUID.fromString(
                Jwts.parserBuilder().setSigningKey(key).build()
                        .parseClaimsJws(token).getBody().getSubject()
        );
    }
}