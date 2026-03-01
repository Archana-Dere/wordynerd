package com.wordynerd.wordynerd.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wordynerd.wordynerd.entity.Word;
import com.wordynerd.wordynerd.repository.WordRepository;

@Service
public class WordService {
    private final WordRepository wordRepository;

    // contructore injection
    public WordService(WordRepository wordRepository){
        this.wordRepository = wordRepository;
    }

    // create word
    public Word createWord(Word word){
        return wordRepository.save(word);
    }

    // get all words
    public List<Word> getAllWords(){
        return wordRepository.findAll();
    }

    // get word by ID
    public Word getWordById(Long id){
        return wordRepository.findById(id).orElseThrow(()-> new RuntimeException("Word not found with the id: "));   
    }

    // delete word
    public void deleteWord(Long id){
        if(!wordRepository.existsById(id)){
            throw new RuntimeException("Word not found with id: "+id);
        }
        wordRepository.deleteById(id);
    }
}
