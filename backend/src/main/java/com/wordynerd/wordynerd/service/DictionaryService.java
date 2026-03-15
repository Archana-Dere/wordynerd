package com.wordynerd.wordynerd.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DictionaryService {
    
    private final RestTemplate restTemplate;

    public DictionaryService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public String searchWord(String word){
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
        return restTemplate.getForObject(url, String.class);
    }
}