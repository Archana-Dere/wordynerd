package com.wordynerd.wordynerd.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wordynerd.wordynerd.dto.LoginRequest;
import com.wordynerd.wordynerd.dto.LoginResponse;
import com.wordynerd.wordynerd.entity.User;
import com.wordynerd.wordynerd.exception.InvalidCredentialsException;
import com.wordynerd.wordynerd.repository.UserRepository;

import com.wordynerd.wordynerd.security.JwtUtil;
import com.wordynerd.wordynerd.dto.SignupRequest;
import com.wordynerd.wordynerd.dto.SignupResponse;


@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public LoginResponse login(LoginRequest request){
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if(optionalUser.isEmpty()){
            throw new InvalidCredentialsException();
        }

        User user = optionalUser.get();

        // if(!user.isActive()){
        //     throw new RuntimeException("Account is disabled");
        // }

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!passwordMatches){
            throw new InvalidCredentialsException();
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());        

        return new LoginResponse(
            user.getId(),
            user.getEmail(),
            user.getRole(),
            token,
            "Login successful"
        );
    }

    public SignupResponse register(SignupRequest request){

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already registered");
        }

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encryptedPassword);
        user.setRole("USER");
        // user.setActive(true);

        User savedUser = userRepository.save(user);

        return new SignupResponse(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getRole(),
            "Signup successfull"
        );
    }
}
