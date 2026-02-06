package com.wordynerd.wordynerd.dto;

public class SignupRequest {

    private String email;
    private String password;

    // default constructor
    public SignupRequest(){

    }

    // getters and setters

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
