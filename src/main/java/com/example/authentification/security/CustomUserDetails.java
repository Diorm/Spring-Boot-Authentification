package com.example.authentification.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final String nom;
    private final String prenom;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(
        String username,
        String password,
        String nom,
        String prenom,
        Collection<? extends GrantedAuthority> authorities
    ) {
        this.username = username;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.authorities = authorities;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}