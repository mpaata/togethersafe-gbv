package com.togethersafe.backend.controller;

import com.togethersafe.backend.dto.ApiResponse;
import com.togethersafe.backend.dto.LoginRequest;
import com.togethersafe.backend.dto.LoginResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        // Temporary mock login for testing (replace with real JWT later)
        if ("admin".equals(request.getUsername()) && "password123".equals(request.getPassword())) {
            return ApiResponse.success(new LoginResponse("fake-token", "admin", "ADMIN"));
        }
        return ApiResponse.error("Invalid credentials");
    }
}