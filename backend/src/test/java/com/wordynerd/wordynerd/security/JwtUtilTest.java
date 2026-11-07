package com.wordynerd.wordynerd.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;

class JwtUtilTest {

    private static final String SECRET =
            Base64.getEncoder().encodeToString(new byte[32]);

    @Test
    void accessTokenContainsTokenVersionAndIsValidated() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        String token = jwtUtil.generateAccessToken(7L, "reader@example.com", "USER", 3);

        Claims claims = jwtUtil.extractAllClaims(token);

        assertEquals("reader@example.com", claims.getSubject());
        assertEquals(7, claims.get("userId", Integer.class));
        assertEquals("USER", claims.get("role", String.class));
        assertEquals(3, claims.get("tokenVersion", Integer.class));
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void rejectsMalformedOrTooShortSigningKeys() {
        assertThrows(IllegalArgumentException.class, () -> new JwtUtil("not-base64"));
        assertThrows(
                IllegalArgumentException.class,
                () -> new JwtUtil(Base64.getEncoder().encodeToString(new byte[16]))
        );
    }

    @Test
    void rejectsTokenSignedWithAnotherKey() {
        JwtUtil issuer = new JwtUtil(SECRET);
        JwtUtil verifier = new JwtUtil(
                Base64.getEncoder().encodeToString(new byte[] {
                        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
                })
        );

        String token = issuer.generateAccessToken(7L, "reader@example.com", "USER", 0);

        assertFalse(verifier.validateToken(token));
    }
}
