package com.wordynerd.wordynerd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordynerd.wordynerd.service.DictionaryService;
import com.wordynerd.wordynerd.dto.DictionaryResponse;


@RestController
@RequestMapping("/dictionary")
public class DictionaryController {
    
    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    // @GetMapping("/search/{word}")
    // public String searchWord(@PathVariable String word){

    //     System.out.println("debug: DICTIONARY CONTROLLER HIT");
    //     return dictionaryService.searchWord(word);
    // }

    @GetMapping("/search/{word}")
    public DictionaryResponse searchWord(@PathVariable String word) {
        System.out.println("debug: DICTIONARY CONTROLLER HIT");
        return dictionaryService.searchWord(word);
}

}
