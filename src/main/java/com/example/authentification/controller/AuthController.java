package com.example.authentification.controller;



import lombok.AllArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.authentification.dto.LoginRequest;
import com.example.authentification.dto.RoleChangeRequest;
import com.example.authentification.dto.UserRegistrationDto;
import com.example.authentification.exception.CustomException;
import com.example.authentification.service.AuthService;
import com.example.authentification.service.UserService;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Ajuste selon tes besoins
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;



  @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto registrationDto) {
        try {
            var user = userService.registerUser(registrationDto);
            return ResponseEntity.ok("Inscription réussie");
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Requête reçue dans /login");
        Map<String, Object> loginResult = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        String message = (String) loginResult.getOrDefault("message", "Connexion réussie");
        String token = (String) loginResult.get("token");
        Map<String, Object> user = (Map<String, Object>) loginResult.get("user");
        LoginResponse response = new LoginResponse(message, user, token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok(authService.logout());
    }

    // ✅ Record mis à jour avec 'token'
    public record LoginResponse(String message, Map<String, Object> user, String token) {}



    @PutMapping("/users/{userId}/role")
    public ResponseEntity<?> updateRole(@PathVariable Long userId, @RequestBody RoleChangeRequest request) {
        userService.changeUserRole(userId, request.getRole());
        return ResponseEntity.ok().build();
    }
}