package com.wordynerd.wordynerd;

import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WordynerdApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private static final String TEST_JWT_SECRET =
			Base64.getEncoder().encodeToString(new byte[32]);

	@DynamicPropertySource
	static void configureJwtSecret(DynamicPropertyRegistry registry) {
		registry.add("wordynerd.jwt.secret", () -> TEST_JWT_SECRET);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void rejectsInvalidRefreshTokens() throws Exception {
		mockMvc.perform(post("/refresh")
						.contentType(APPLICATION_JSON)
						.content("{\"refreshToken\":\"invalid-token\"}"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Invalid or expired refresh token"));
	}

	@Test
	void requiresAuthenticationForProtectedEndpoints() throws Exception {
		mockMvc.perform(get("/user/me"))
				.andExpect(status().isUnauthorized());
	}
}
