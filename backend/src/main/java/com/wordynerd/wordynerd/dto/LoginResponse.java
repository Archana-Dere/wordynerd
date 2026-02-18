package com.wordynerd.wordynerd.dto;

public class LoginResponse {

    private Long userId;
    private String email;
    private String role;
    private String accessToken;
    private String refreshToken;
    private String message;

    // Default constructor
    public LoginResponse() {
    }

    // All-args constructor (used in service)
    public LoginResponse(Long userId,
                         String email,
                         String role,
                         String accessToken,
                         String refreshToken,
                         String message) {

        this.userId = userId;
        this.email = email;
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = message;
    }

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // ✅ Correct getter name
    public String getAccessToken() {
        return accessToken;
    }

    // ✅ Proper setter
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    // ✅ Correct getter
    public String getRefreshToken() {
        return refreshToken;
    }

    // ✅ Correct setter (must accept parameter)
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
