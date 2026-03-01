package com.wordynerd.wordynerd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wordynerd.wordynerd.entity.Word;

public interface WordRepository extends JpaRepository<Word, Long> {
    
}
