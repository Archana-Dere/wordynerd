package com.wordynerd.wordynerd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // .requestMatchers("/health-check").permitAll()
                // .requestMatchers("/signup").permitAll()
                // .requestMatchers("/login").permitAll()
                // .anyRequest().authenticated()
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
