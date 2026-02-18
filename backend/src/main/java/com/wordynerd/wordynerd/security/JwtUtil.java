package com.wordynerd.wordynerd.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET =
            "wordynerd_wordynerd_wordynerd_secret_123456";

    private static final long ACCESS_EXPIRATION =
            1000 * 60 * 15; // 15 minutes

    private static final long REFRESH_EXPIRATION =
            1000L * 60 * 60 * 24 * 7; // 7 days

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateAccessToken(Long userId, String email, String role) {

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + ACCESS_EXPIRATION)
                )
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId) {

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + REFRESH_EXPIRATION)
                )
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
    return extractAllClaims(token).getSubject();
        }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
