package com.example.authentification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.authentification.controller.AuthController;
import com.example.authentification.dto.LoginRequest;
import com.example.authentification.dto.RegistrationDto;
import com.example.authentification.entity.Role;
import com.example.authentification.entity.RoleEnum;
import com.example.authentification.entity.User;
import com.example.authentification.exception.CustomException;
import com.example.authentification.repository.RoleRepository;
import com.example.authentification.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService; // Assurez-vous d'avoir un service JWT pour générer des tokens

    public Map<String, Object> login(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        String token = jwtService.generateToken(user); // ⬅️ Utilise la version avec rôles

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Connexion réussie");
        result.put("token", token);
        result.put("user", Map.of(
                "email", user.getEmail(),
                "roles", user.getRoles(),
                "prenom", user.getPrenom(),
                "nom", user.getNom()));

        return result;
    }

    public String logout() {
        SecurityContextHolder.clearContext();
        return "Déconnecté avec succès";
    }

    // Registeration method
    @Transactional
    public User registerUser(RegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new CustomException("Email déjà utilisé !");
        }

        User user = new User();
        user.setNom(registrationDto.getNom());
        user.setPrenom(registrationDto.getPrenom());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getMotDePasse()));
        user.setEnabled(true);
        user.setDateInscription(new Date());

        Role defaultRole = roleRepository.findByName(RoleEnum.ROLE_ETUDIANT)
                .orElseThrow(() -> new CustomException("Rôle par défaut non trouvé"));
        user.setRoles(Set.of(defaultRole));

        return userRepository.save(user);
    }


    //Rafraîchir le token
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String oldToken = extractTokenFromHeader(request);
        String newToken = jwtService.refreshToken(oldToken);
        return ResponseEntity.ok().body(Map.of("token", newToken));
    }

    // Méthode utilitaire pour extraire le token du header Authorization
    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new CustomException("Token JWT manquant ou invalide dans l'en-tête Authorization");
    }
}