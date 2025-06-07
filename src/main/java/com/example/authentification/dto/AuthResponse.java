package com.example.authentification.dto;


import java.util.Map;

public class AuthResponse {
    private String token;
    private Map<String, Object> user;
    private String message;

    public String getToken() {
        return token;
    }

    public Map<String, Object> getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(Map<String, Object> user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthResponse(String token, Map<String, Object> user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    public AuthResponse() {
    }

    

}