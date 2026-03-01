package com.wordynerd.wordynerd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wordynerd.wordynerd.entity.User;
import com.wordynerd.wordynerd.entity.Word;

public interface WordRepository extends JpaRepository<Word, Long> {

    // ✅ Fetch words belonging to a specific user
    List<Word> findByUser(User user);

}