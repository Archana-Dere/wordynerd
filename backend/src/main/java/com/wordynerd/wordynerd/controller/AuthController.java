package com.wordynerd.wordynerd.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wordynerd.wordynerd.dto.LoginRequest;
import com.wordynerd.wordynerd.dto.LoginResponse;
import com.wordynerd.wordynerd.dto.SignupRequest;
import com.wordynerd.wordynerd.dto.SignupResponse;
import com.wordynerd.wordynerd.service.UserService;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/signup")
    public SignupResponse signup(@RequestBody SignupRequest request) {
        return userService.register(request);
    }
}
