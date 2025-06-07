// package com.example.gestion_des_etudiant.service;


// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.example.gestion_des_etudiant.entity.Role;
// import com.example.gestion_des_etudiant.entity.Utilisateur;
// import com.example.gestion_des_etudiant.repository.RoleRepository;
// import com.example.gestion_des_etudiant.repository.UtilisateurRepository;

// import java.util.List;
// import java.util.Optional;
// import java.util.Set;

// @Service
// @Transactional
// public class AdminService {
    
//     @Autowired
//     private UtilisateurRepository utilisateurRepository;
    
//     @Autowired
//     private RoleRepository roleRepository;
    
//     public List<Utilisateur> getPendingUsers() {
//         return utilisateurRepository.findPendingUsersOrderedByDate();
//     }
    
//     public List<Utilisateur> getAllUsers() {
//         return utilisateurRepository.findAll();
//     }
    
//     public long getPendingUsersCount() {
//         return utilisateurRepository.countPendingUsers();
//     }
    
//     public String approveUser(Long userId) {
//         Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(userId);
        
//         if (!utilisateurOpt.isPresent()) {
//             throw new RuntimeException("Utilisateur non trouvé");
//         }
        
//         Utilisateur utilisateur = utilisateurOpt.get();
        
//         if (utilisateur.getEnabled()) {
//             throw new RuntimeException("Cet utilisateur est déjà approuvé");
//         }
        
//         utilisateur.setEnabled(true);
//         utilisateurRepository.save(utilisateur);
        
//         return "Utilisateur approuvé avec succès";
//     }
    
//     public String rejectUser(Long userId) {
//         Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(userId);
        
//         if (!utilisateurOpt.isPresent()) {
//             throw new RuntimeException("Utilisateur non trouvé");
//         }
        
//         utilisateurRepository.deleteById(userId);
        
//         return "Utilisateur rejeté et supprimé";
//     }
    
//     public String addRoleToUser(Long userId, String roleName) {
//         Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(userId);
        
//         if (!utilisateurOpt.isPresent()) {
//             throw new RuntimeException("Utilisateur non trouvé");
//         }
        
//         Role.RoleName roleEnum;
//         try {
//             roleEnum = Role.RoleName.valueOf(roleName);
//         } catch (IllegalArgumentException e) {
//             throw new RuntimeException("Rôle invalide: " + roleName);
//         }
        
//         Optional<Role> roleOpt = roleRepository.findByNom(roleEnum);
        
//         if (!roleOpt.isPresent()) {
//             throw new RuntimeException("Rôle non trouvé");
//         }
        
//         Utilisateur utilisateur = utilisateurOpt.get();
//         Role role = roleOpt.get();
        
//         if (utilisateur.getRoles().contains(role)) {
//             throw new RuntimeException("L'utilisateur possède déjà ce rôle");
//         }
        
//         utilisateur.addRole(role);
//         utilisateurRepository.save(utilisateur);
        
//         return "Rôle ajouté avec succès";
//     }
    
//     public String removeRoleFromUser(Long userId, String roleName) {
//         Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(userId);
        
//         if (!utilisateurOpt.isPresent()) {
//             throw new RuntimeException("Utilisateur non trouvé");
//         }
        
//         Role.RoleName roleEnum;
//         try {
//             roleEnum = Role.RoleName.valueOf(roleName);
//         } catch (IllegalArgumentException e) {
//             throw new RuntimeException("Rôle invalide: " + roleName);
//         }
        
//         Optional<Role> roleOpt = roleRepository.findByNom(roleEnum);
        
//         if (!roleOpt.isPresent()) {
//             throw new RuntimeException("Rôle non trouvé");
//         }
        
//         Utilisateur utilisateur = utilisateurOpt.get();
//         Role role = roleOpt.get();
        
//         if (!utilisateur.getRoles().contains(role)) {
//             throw new RuntimeException("L'utilisateur ne possède pas ce rôle");
//         }
        
//         // Vérifier qu'il garde au moins un rôle
//         if (utilisateur.getRoles().size() <= 1) {
//             throw new RuntimeException("L'utilisateur doit conserver au moins un rôle");
//         }
        
//         utilisateur.removeRole(role);
//         utilisateurRepository.save(utilisateur);
        
//         return "Rôle retiré avec succès";
//     }
    
//     public String toggleUserStatus(Long userId) {
//         Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(userId);
        
//         if (!utilisateurOpt.isPresent()) {
//             throw new RuntimeException("Utilisateur non trouvé");
//         }
        
//         Utilisateur utilisateur = utilisateurOpt.get();
//         utilisateur.setEnabled(!utilisateur.getEnabled());
//         utilisateurRepository.save(utilisateur);
        
//         return utilisateur.getEnabled() ? "Utilisateur activé" : "Utilisateur désactivé";
//     }
    
//     public List<Utilisateur> getUsersByRole(String roleName) {
//         Role.RoleName roleEnum;
//         try {
//             roleEnum = Role.RoleName.valueOf(roleName);
//         } catch (IllegalArgumentException e) {
//             throw new RuntimeException("Rôle invalide: " + roleName);
//         }
        
//         return utilisateurRepository.findByRoleName(roleEnum);
//     }
// }
