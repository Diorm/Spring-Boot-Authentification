package com.example.authentification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.authentification.dto.UserRegistrationDto;
import com.example.authentification.entity.Role;
import com.example.authentification.entity.RoleEnum;
import com.example.authentification.entity.User;
import com.example.authentification.exception.CustomException;
import com.example.authentification.repository.RoleRepository;
import com.example.authentification.repository.UserRepository;

import java.util.*;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserRegistrationDto registrationDto) {
        System.out.println("Tentative d'inscription : " + registrationDto.getEmail() + " avec rôles: " + registrationDto.getRoles());
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new CustomException("Email déjà utilisé !");
        }

        Set<Role> roles = new HashSet<>();
        if (registrationDto.getRoles() == null || registrationDto.getRoles().isEmpty()) {
            // Rôle par défaut
            roles.add(findOrCreateRole(RoleEnum.ROLE_ETUDIANT));
        } else {
            for (String roleStr : registrationDto.getRoles()) {
                try {
                    RoleEnum roleEnum = RoleEnum.valueOf(roleStr.toUpperCase());
                    roles.add(findOrCreateRole(roleEnum));
                } catch (IllegalArgumentException e) {
                    throw new CustomException("Rôle invalide: " + roleStr);
                }
            }
        }

        User user = new User();
        user.setNom(registrationDto.getNom());
        user.setPrenom(registrationDto.getPrenom());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEnabled(true);
        user.setDateInscription(new Date());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    private Role findOrCreateRole(RoleEnum roleName) {
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        if (roleOpt.isPresent()) {
            return roleOpt.get();
        } else {
            Role newRole = new Role();
            newRole.setName(roleName);
            return roleRepository.save(newRole);
        }
    }


    //Pour changer de role
    public void changeUserRole(Long userId, String roleName, boolean add) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException("Utilisateur non trouvé"));

    Role role = roleRepository.findByName(RoleEnum.valueOf(roleName))
        .orElseThrow(() -> new CustomException("Rôle introuvable"));

    if (add) {
        user.getRoles().add(role);
    } else {
        user.getRoles().remove(role);
    }

    userRepository.save(user);
}

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException("Utilisateur non trouvé"));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("Utilisateur non trouvé"));
        userRepository.delete(user);
    }
}
