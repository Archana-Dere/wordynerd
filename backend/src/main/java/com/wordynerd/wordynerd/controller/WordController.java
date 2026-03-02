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

        @PutMapping("/{id}")
        public Word updateWord(@PathVariable Long id,
                            @RequestBody Word word) {
            return wordService.updateWord(id, word);
        }

        @DeleteMapping("/{id}")
        public void deleteWord(@PathVariable Long id) {
            wordService.deleteWord(id);
        }
    }
    
