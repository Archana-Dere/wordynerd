package com.wordynerd.wordynerd.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.wordynerd.wordynerd.entity.Word;
import com.wordynerd.wordynerd.service.WordService;

@RestController
@RequestMapping("/api/words")
    public class WordController {
        private final WordService wordService;

        public WordController(WordService wordService){
            this.wordService = wordService;
        }

        @PostMapping
        public Word createWord(@RequestBody Word word) {
            return wordService.createWord(word);
        }

        @GetMapping
        public List<Word> getAllWords() {
            return wordService.getAllWords();
        }

        @GetMapping("/{id}")
        public Word getWordById(@PathVariable Long id) {
            return wordService.getWordById(id);
        }

        @DeleteMapping("{id}")
        public String deleteWord(@PathVariable Long id) {
            wordService.deleteWord(id);
            return "Word deleted successfully.";
        }
    }
    
