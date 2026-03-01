package com.wordynerd.wordynerd.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.wordynerd.wordynerd.entity.User;
import com.wordynerd.wordynerd.entity.Word;
import com.wordynerd.wordynerd.repository.UserRepository;
import com.wordynerd.wordynerd.repository.WordRepository;

@Service
public class WordService {

    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    // Constructor injection
    public WordService(WordRepository wordRepository,
                       UserRepository userRepository) {
        this.wordRepository = wordRepository;
        this.userRepository = userRepository;
    }

    // ✅ Create word (attach logged-in user)
    public Word createWord(Word word) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        word.setUser(user);

        return wordRepository.save(word);
    }

    // ✅ Get only logged-in user's words
    public List<Word> getAllWords() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return wordRepository.findByUser(user);
    }

    // Get word by ID (optional improvement: restrict by user later)
    public Word getWordById(Long id) {
        return wordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Word not found with id: " + id));
    }

    // Delete word (optional improvement: restrict by user later)
    public void deleteWord(Long id) {

        if (!wordRepository.existsById(id)) {
            throw new RuntimeException("Word not found with id: " + id);
        }

        wordRepository.deleteById(id);
    }
}