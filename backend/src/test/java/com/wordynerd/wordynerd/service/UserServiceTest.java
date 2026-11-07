package com.wordynerd.wordynerd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wordynerd.wordynerd.dto.LoginResponse;
import com.wordynerd.wordynerd.entity.User;
import com.wordynerd.wordynerd.repository.UserRepository;
import com.wordynerd.wordynerd.security.JwtUtil;
import com.wordynerd.wordynerd.service.RefreshTokenService.Rotation;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenService refreshTokenService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, jwtUtil, refreshTokenService);
    }

    @Test
    void logoutInvalidatesAccessTokensAndDeletesRefreshToken() {
        User user = new User();
        user.setId(12L);
        user.setEmail("reader@example.com");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        userService.logout(user.getEmail());

        assertEquals(1, user.getTokenVersion());
        verify(userRepository).save(user);
        verify(refreshTokenService).deleteByUserId(user.getId());
    }

    @Test
    void refreshRotatesRefreshTokenAndIssuesAccessTokenWithCurrentVersion() {
        User user = new User();
        user.setId(12L);
        user.setEmail("reader@example.com");
        user.setRole("USER");
        user.setTokenVersion(4);
        when(refreshTokenService.rotateRefreshToken("old-refresh"))
                .thenReturn(new Rotation(user.getId(), "new-refresh"));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getRole(), 4))
                .thenReturn("new-access");

        LoginResponse response = userService.refresh("old-refresh");

        assertEquals("new-access", response.getAccessToken());
        assertEquals("new-refresh", response.getRefreshToken());
        assertNotNull(response.getUserId());
        verify(jwtUtil).generateAccessToken(user.getId(), user.getEmail(), user.getRole(), 4);
    }
}
