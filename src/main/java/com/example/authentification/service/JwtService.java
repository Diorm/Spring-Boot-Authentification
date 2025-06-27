package com.example.authentification.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.authentification.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


import java.security.Key;

@Service
public class JwtService {

    // 🔑 Clé secrète au format Base64 (générée avec
    // Keys.secretKeyFor(SignatureAlgorithm.HS256))
    private final String SECRET_KEY = "jvPeFfP7WuVToKYMZabH9hE8lGqWXNtLZUdJXsRzj3mQxgA=";

    // ⏱️ Durée de validité du token : 5 heures
    private final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 24000; // en millisecondes

    // Extraire le nom d'utilisateur (email) depuis le token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraire une claim spécifique
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Génère un token à partir de l'email de l'utilisateur
    // public String generateToken(User user) {
    // Map<String, Object> claims = new HashMap<>();
    // claims.put("roles", user.getRoles().stream()
    // .map(role -> role.getName().name()) // ex: "ROLE_ADMIN"
    // .collect(Collectors.toList()));
    // return createToken(claims, user.getEmail());
    // }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList()));

        return createToken(claims, user.getEmail());
    }

    // Génère un token à partir de l'email de l'utilisateur et de ses rôl
    // Construit le token JWT avec les claims et la durée de vie
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Valide si le token appartient à l'utilisateur et n’est pas expiré
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Vérifie si le token est expiré
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extraire la date d'expiration
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraire toutes les claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Récupère la clé de signature
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Méthode pour rafraîchir le token (à implémenter)
    public String refreshToken(String oldToken) {
        // Extraire les claims existantes du token expiré
        Claims claims = extractAllClaims(oldToken);

        // Régénère un token avec les mêmes données, mais nouvelle expiration
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}