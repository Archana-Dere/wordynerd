package com.wordynerd.wordynerd.dto;

public class DictionaryResponse {

    private String word;
    private String phonetic;
    private String meaning;
    private String example;

    public DictionaryResponse() {
    }

    public DictionaryResponse(String word, String phonetic, String meaning, String example) {
        this.word = word;
        this.phonetic = phonetic;
        this.meaning = meaning;
        this.example = example;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}