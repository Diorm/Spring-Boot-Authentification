package com.example.authentification.config;


import java.util.Map;

public record LoginResponse(String message, Map<String, Object> user, String token) {}
