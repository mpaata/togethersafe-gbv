package com.togethersafe.backend.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private T data;
    private String error;
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        return response;
    }
    
    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setError(message);
        return response;
    }
}