package com.example.authentification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordDto {
    
    @NotBlank(message = "Le token est obligatoire")
    private String token;
    
    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String nouveauMotDePasse;
    
    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    private String confirmationMotDePasse;
    
    // Constructeurs
    public ResetPasswordDto() {}
    
    public ResetPasswordDto(String token, String nouveauMotDePasse, 
                           String confirmationMotDePasse) {
        this.token = token;
        this.nouveauMotDePasse = nouveauMotDePasse;
        this.confirmationMotDePasse = confirmationMotDePasse;
    }
    
    // Getters et Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getNouveauMotDePasse() { return nouveauMotDePasse; }
    public void setNouveauMotDePasse(String nouveauMotDePasse) { 
        this.nouveauMotDePasse = nouveauMotDePasse; 
    }
    
    public String getConfirmationMotDePasse() { return confirmationMotDePasse; }
    public void setConfirmationMotDePasse(String confirmationMotDePasse) { 
        this.confirmationMotDePasse = confirmationMotDePasse; 
    }
    
    // Validation
    public boolean isPasswordMatching() {
        return nouveauMotDePasse != null && 
               nouveauMotDePasse.equals(confirmationMotDePasse);
    }
}
