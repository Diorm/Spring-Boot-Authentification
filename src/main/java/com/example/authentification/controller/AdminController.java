package com.example.authentification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authentification.dto.RoleChangeRequest;
import com.example.authentification.entity.User;
import com.example.authentification.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<?> updateRole(@PathVariable Long userId, @RequestBody RoleChangeRequest request) {
        userService.changeUserRole(userId, request.getRole(), request.isAdd());
        return ResponseEntity.ok().build();
    }
}