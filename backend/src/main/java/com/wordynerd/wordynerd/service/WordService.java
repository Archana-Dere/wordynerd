package com.wordynerd.wordynerd.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ ADDED

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

    // ✅ UPDATED: Get word by ID (now restricted to owner)
    public Word getWordById(Long id) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Word not found"));

        // 🔥 OWNER CHECK ADDED
        if (!word.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to access this word");
        }

        return word;
    }

    // ✅ NEW: Update word (OWNER ONLY)
    @Transactional
    public Word updateWord(Long id, Word updatedWord) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Word existingWord = wordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Word not found"));

                System.out.println("Logged in user email: " + user.getEmail());
                System.out.println("Word owner ID: " + existingWord.getUser().getId());

        // 🔥 OWNER CHECK
        if (!existingWord.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to update this word");
        }

        existingWord.setWord(updatedWord.getWord());
        existingWord.setMeaning(updatedWord.getMeaning());
        existingWord.setExampleSentence(updatedWord.getExampleSentence());
        existingWord.setDifficulty(updatedWord.getDifficulty());

        return wordRepository.save(existingWord);
    }

    // ✅ UPDATED: Delete word (OWNER ONLY)
    @Transactional
    public void deleteWord(Long id) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Word not found"));

        // 🔥 OWNER CHECK
        if (!word.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to delete this word");
        }

        wordRepository.delete(word);
    }
}