package com.wordynerd.wordynerd.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wordynerd.wordynerd.entity.RefreshToken;
import com.wordynerd.wordynerd.exception.InvalidRefreshTokenException;
import com.wordynerd.wordynerd.repository.RefreshTokenRepository;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        refreshTokenService = new RefreshTokenService(refreshTokenRepository);
    }

    @Test
    void storesOnlyAHashAndRotatesRefreshTokensOnce() {
        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        String originalToken = refreshTokenService.createRefreshToken(12L);
        ArgumentCaptor<RefreshToken> initialCaptor =
                ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(initialCaptor.capture());
        RefreshToken storedToken = initialCaptor.getValue();

        assertNotEquals(originalToken, storedToken.getTokenHash());
        assertTrue(storedToken.getTokenHash().matches("[0-9a-f]{64}"));

        clearInvocations(refreshTokenRepository);
        when(refreshTokenRepository.findByTokenHash(anyString()))
                .thenReturn(Optional.of(storedToken), Optional.empty());

        RefreshTokenService.Rotation rotation =
                refreshTokenService.rotateRefreshToken(originalToken);

        assertNotEquals(originalToken, rotation.refreshToken());
        verify(refreshTokenRepository).deleteByUserId(12L);
        assertThrows(
                InvalidRefreshTokenException.class,
                () -> refreshTokenService.rotateRefreshToken(originalToken)
        );
    }

    @Test
    void rejectsExpiredRefreshTokens() {
        RefreshToken expiredToken = new RefreshToken(
                12L,
                "hash",
                Instant.now().minusSeconds(1)
        );
        when(refreshTokenRepository.findByTokenHash(anyString()))
                .thenReturn(Optional.of(expiredToken));

        assertThrows(
                InvalidRefreshTokenException.class,
                () -> refreshTokenService.rotateRefreshToken("expired-token")
        );
    }
}
