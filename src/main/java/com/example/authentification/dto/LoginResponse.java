package com.example.authentification.dto;


import java.util.Map;

public class LoginResponse {
    private String message;
    private Map<String, Object> user;

    public LoginResponse(String message, Map<String, Object> user) {
        this.message = message;
        this.user = user;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getUser() {
        return user;
    }

    public void setUser(Map<String, Object> user) {
        this.user = user;
    }
}
