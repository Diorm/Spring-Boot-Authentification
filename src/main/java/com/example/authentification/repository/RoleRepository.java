package com.example.authentification.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authentification.entity.Role;
import com.example.authentification.entity.RoleEnum;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum name);
}
