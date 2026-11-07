package com.wordynerd.wordynerd.security;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Base64;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.security.core.context.SecurityContextHolder;

import com.wordynerd.wordynerd.entity.User;
import com.wordynerd.wordynerd.repository.UserRepository;

class JwtAuthenticationFilterTest {

    private static final String SECRET =
            Base64.getEncoder().encodeToString(new byte[32]);

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void rejectsAccessTokensAfterLogoutIncrementsUserVersion() throws Exception {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        UserRepository userRepository = org.mockito.Mockito.mock(UserRepository.class);
        User user = new User();
        user.setId(12L);
        user.setEmail("reader@example.com");
        String token = jwtUtil.generateAccessToken(12L, user.getEmail(), "USER", 0);
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        JwtAuthenticationFilter filter =
                new JwtAuthenticationFilter(jwtUtil, userRepository);

        MockHttpServletRequest validRequest = new MockHttpServletRequest();
        validRequest.addHeader("Authorization", "Bearer " + token);
        filter.doFilter(validRequest, new MockHttpServletResponse(), new MockFilterChain());

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        SecurityContextHolder.clearContext();

        user.setTokenVersion(1);
        MockHttpServletRequest revokedRequest = new MockHttpServletRequest();
        revokedRequest.addHeader("Authorization", "Bearer " + token);
        filter.doFilter(revokedRequest, new MockHttpServletResponse(), new MockFilterChain());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
