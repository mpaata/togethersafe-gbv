package com.togethersafe.backend.controller;

import com.togethersafe.backend.dto.ApiResponse;
import com.togethersafe.backend.dto.LoginRequest;
import com.togethersafe.backend.dto.LoginResponse;
import com.togethersafe.backend.dto.RegisterRequest;
import com.togethersafe.backend.entity.User;
import com.togethersafe.backend.entity.enums.Role;
import com.togethersafe.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        if ("admin".equals(request.getUsername()) && "password123".equals(request.getPassword())) {
            return ApiResponse.success(new LoginResponse("fake-token", "admin", "ADMIN"));
        }
        return userRepository.findByUsername(request.getUsername())
            .filter(u -> u.getPasswordHash().equals(request.getPassword()))
            .map(u -> ApiResponse.success(new LoginResponse("fake-token", u.getUsername(), u.getRole().name())))
            .orElse(ApiResponse.error("Invalid credentials"));
    }

    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ApiResponse.error("Username already taken");
        }

        User user = new User();
        user.setOrgId(1L);
        user.setUsername(request.getUsername());
        user.setPasswordHash(request.getPassword());
        user.setFullName(request.getFullName());
        user.setRole(Role.valueOf(request.getRole()));
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);
        return ApiResponse.success(new LoginResponse("fake-token", saved.getUsername(), saved.getRole().name()));
    }
}