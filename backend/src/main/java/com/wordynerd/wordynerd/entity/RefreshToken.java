package com.wordynerd.wordynerd.entity;


import java.time.Instant; 
import jakarta.persistence.*;

import java.lang.annotation.Inherited;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(unique = true, nullable = false)
    private String token;

    private Instant expiryDate;

    public RefreshToken() {}

    public RefreshToken(Long userId, String token, Instant expiryDate){
        this.userId = userId;
        this.token = token;
        this.expiryDate = expiryDate;
    }
    
    //getters and setters
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
}
