package com.wordynerd.wordynerd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wordynerd.wordynerd.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteByUserId(Long userId);

}
