package com.wordynerd.wordynerd.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.wordynerd.wordynerd.entity.RefreshToken;
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

    public RefreshToken createRefreshToken(Long userId) {

        refreshTokenRepository.deleteByUserId(userId);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(
                Instant.now().plusMillis(REFRESH_TOKEN_DURATION)
        );

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }

        return token;
    }
}
