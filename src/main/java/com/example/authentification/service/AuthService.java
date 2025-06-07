package com.example.authentification.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.authentification.controller.AuthController;
import com.example.authentification.dto.LoginRequest;
import com.example.authentification.dto.RegistrationDto;
import com.example.authentification.entity.Role;
import com.example.authentification.entity.RoleEnum;
import com.example.authentification.entity.User;
import com.example.authentification.exception.CustomException;
import com.example.authentification.repository.RoleRepository;
import com.example.authentification.repository.UserRepository;

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
        Authentication authenticate = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );

        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

        String token = jwtService.generateToken(userDetails.getUsername());

        Map<String, Object> userData = new HashMap<>();
        userData.put("email", userDetails.getUsername());
        userData.put("roles", userDetails.getAuthorities());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", userData);
        response.put("message", "Connexion réussie");

        return response;
    }

    public String logout() {
        SecurityContextHolder.clearContext();
        return "Déconnecté avec succès";
    }


    //Registeration method
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
}