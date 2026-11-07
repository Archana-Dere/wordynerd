package com.wordynerd.wordynerd.entity;


import java.time.Instant; 
import jakarta.persistence.*;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(name = "token", unique = true, nullable = false)
    private String tokenHash;

    private Instant expiryDate;

    public RefreshToken() {}

    public RefreshToken(Long userId, String tokenHash, Instant expiryDate){
        this.userId = userId;
        this.tokenHash = tokenHash;
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

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
}
