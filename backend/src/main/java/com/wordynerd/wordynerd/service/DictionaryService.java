package com.wordynerd.wordynerd.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.wordynerd.wordynerd.dto.DictionaryResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// import com.wordynerd.wordynerd.dto.DictionaryResponse;

@Service
public class DictionaryService {

    private final RestTemplate restTemplate;

    public DictionaryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DictionaryResponse searchWord(String word) {

        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

        // String response = restTemplate.getForObject(url, String.class);

        DictionaryResponse dto = new DictionaryResponse();
        try {
        String response = restTemplate.getForObject(url, String.class);

        // dto.setWord(word);
        // dto.setMeaning(response); // temporary

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);

        JsonNode firstEntry = root.get(0);

        dto.setWord(firstEntry.get("word").asText());

        JsonNode phonetics = firstEntry.get("phonetics");
        if(phonetics.isArray() && phonetics.size() > 0){
            JsonNode phoneticNode = phonetics.get(0);
            if(phoneticNode.has("text")){
                dto.setPhonetic(phoneticNode.get("text").asText());
            }
        }

        // meaning
        JsonNode meanings = firstEntry.get("meanings");
        if (meanings.isArray() && meanings.size() > 0) {
            JsonNode definitions = meanings.get(0).get("definitions");
            if (definitions.isArray() && definitions.size() > 0) {

                JsonNode firstDef = definitions.get(0);

                if (firstDef.has("definition")) {
                    dto.setMeaning(firstDef.get("definition").asText());
                }

                if (firstDef.has("example")) {
                    dto.setExample(firstDef.get("example").asText());
                }
            }
        }

    } catch (HttpClientErrorException ex) {

        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            dto.setWord(word);
            dto.setMeaning("Word not found");
        } else {
            throw ex;
        }

    } catch (Exception e) {
        throw new RuntimeException("Error parsing dictionary response", e);
    }

    return dto;
    }
}