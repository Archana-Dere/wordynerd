package com.wordynerd.wordynerd.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wordynerd.wordynerd.entity.RefreshToken;
import com.wordynerd.wordynerd.exception.InvalidRefreshTokenException;
import com.wordynerd.wordynerd.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // CONSTRUCTOR
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    private static final long REFRESH_TOKEN_DURATION =
            1000L * 60 * 60 * 24 * 7; // 7 days

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Transactional
    public String createRefreshToken(Long userId) {
        String rawToken = generateToken();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setTokenHash(hashToken(rawToken));
        refreshToken.setExpiryDate(
                Instant.now().plusMillis(REFRESH_TOKEN_DURATION)
        );

        refreshTokenRepository.deleteByUserId(userId);
        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    @Transactional
    public Rotation rotateRefreshToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            throw new InvalidRefreshTokenException();
        }

        RefreshToken currentToken = refreshTokenRepository
                .findByTokenHash(hashToken(rawToken))
                .orElseThrow(InvalidRefreshTokenException::new);

        if (currentToken.getExpiryDate() == null
                || !currentToken.getExpiryDate().isAfter(Instant.now())) {
            throw new InvalidRefreshTokenException();
        }

        Long userId = currentToken.getUserId();
        String nextToken = createRefreshToken(userId);
        return new Rotation(userId, nextToken);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    private String generateToken() {
        byte[] tokenBytes = new byte[32];
        SECURE_RANDOM.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    private String hashToken(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }

    public record Rotation(Long userId, String refreshToken) {
    }
}
