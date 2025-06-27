package com.example.authentification.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.authentification.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
        @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
        Optional<User> findByEmailWithRoles(String email);
        Boolean existsByEmail(String email);
}