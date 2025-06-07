// package com.example.gestion_des_etudiant.repository;


// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;

// import com.example.gestion_des_etudiant.entity.ResetToken;
// import com.example.gestion_des_etudiant.entity.Utilisateur;

// import java.time.LocalDateTime;
// import java.util.Optional;

// @Repository
// public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    
//     Optional<ResetToken> findByToken(String token);
    
//     Optional<ResetToken> findByUtilisateur(Utilisateur utilisateur);
    
//     @Modifying
//     @Query("DELETE FROM ResetToken r WHERE r.expiration < :now OR r.utilise = true")
//     void deleteExpiredAndUsedTokens(LocalDateTime now);
    
//     @Modifying
//     @Query("DELETE FROM ResetToken r WHERE r.utilisateur = :utilisateur")
//     void deleteByUtilisateur(Utilisateur utilisateur);
// }