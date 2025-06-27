package com.example.authentification.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.authentification.service.CustomUserDetailsService;
import com.example.authentification.service.JwtService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("JWT Filter - Requête entrante : {}", request.getRequestURI());

        String requestURI = request.getRequestURI();

        // Ignorer les routes publiques
        if (requestURI.contains("/api/auth/login") || requestURI.contains("/api/auth/register")) {
            logger.info("Ignorer le filtre JWT pour : {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7); // Retire "Bearer "
            try {
                email = jwtService.extractUsername(token); // Extrait l'email
            } catch (ExpiredJwtException e) {
                logger.warn("Token expiré", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expiré");
                return;
            } catch (JwtException e) {
                logger.warn("Erreur lors de l'extraction du token", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide");
                return;
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                logger.info("Authorities chargées : {}", userDetails.getAuthorities());

                if (jwtService.validateToken(token, userDetails)) {
                    // 🔽 EXTRAIRE LES RÔLES DU TOKEN
                    List<String> roles = jwtService.extractClaim(token, claims -> claims.get("roles", List.class));
                    logger.info("Rôles extraits du token : {}", roles);

                    // 🔽 CONVERTIR EN AUTHORITIES
                    List<GrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("Authentification mise à jour dans le contexte de sécurité pour : {} avec rôles : {}",
                            email, authorities);
                } else {
                    logger.warn("Token non valide pour l'utilisateur : {}", email);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token non valide");
                    return;
                }
            } catch (Exception e) {
                logger.error("Erreur lors du traitement de l'authentification : {}", e.getMessage(), e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erreur d'authentification");
                return;
            }
        }

        // ⚠️ IMPORTANT : Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }



    
}