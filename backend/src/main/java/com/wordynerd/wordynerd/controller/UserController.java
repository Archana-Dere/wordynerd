package com.wordynerd.wordynerd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wordynerd.wordynerd.dto.UserResponse;
import com.wordynerd.wordynerd.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public UserResponse getCurrentUser() {
        return userService.getLoggedInUser();
    }
}
