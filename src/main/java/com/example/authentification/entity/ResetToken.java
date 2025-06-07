// package com.example.gestion_des_etudiant.entity;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// @Entity
// @Table(name = "reset_tokens")
// public class ResetToken {
    
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;
    
//     @Column(unique = true, nullable = false)
//     private String token;
    
//     @OneToOne(fetch = FetchType.EAGER)
//     @JoinColumn(name = "utilisateur_id", nullable = false)
//     private Utilisateur utilisateur;
    
//     @Column(nullable = false)
//     private LocalDateTime expiration;
    
//     @Column(nullable = false)
//     private Boolean utilise = false;
    
//     // Constructeurs
//     public ResetToken() {}
    
//     public ResetToken(String token, Utilisateur utilisateur, LocalDateTime expiration) {
//         this.token = token;
//         this.utilisateur = utilisateur;
//         this.expiration = expiration;
//     }
    
//     // Getters et Setters
//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }
    
//     public String getToken() { return token; }
//     public void setToken(String token) { this.token = token; }
    
//     public Utilisateur getUtilisateur() { return utilisateur; }
//     public void setUtilisateur(Utilisateur utilisateur) { 
//         this.utilisateur = utilisateur; 
//     }
    
//     public LocalDateTime getExpiration() { return expiration; }
//     public void setExpiration(LocalDateTime expiration) { 
//         this.expiration = expiration; 
//     }
    
//     public Boolean getUtilise() { return utilise; }
//     public void setUtilise(Boolean utilise) { this.utilise = utilise; }
    
//     // Méthodes utilitaires
//     public boolean isExpired() {
//         return LocalDateTime.now().isAfter(expiration);
//     }
    
//     public boolean isValid() {
//         return !utilise && !isExpired();
//     }
// }