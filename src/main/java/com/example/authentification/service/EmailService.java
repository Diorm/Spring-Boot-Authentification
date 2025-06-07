// package com.example.gestion_des_etudiant.service;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.stereotype.Service;

// import com.example.gestion_des_etudiant.entity.Utilisateur;

// import jakarta.mail.MessagingException;
// import jakarta.mail.internet.MimeMessage;
// import java.util.List;

// @Service
// public class EmailService {
    
//     @Autowired
//     private JavaMailSender mailSender;
    
//     @Value("${spring.mail.username}")
//     private String fromEmail;
    
//     @Value("${app.frontend.url:http://localhost:3000}")
//     private String frontendUrl;
    
//     public void sendPasswordResetEmail(Utilisateur utilisateur, String token) {
//         try {
//             MimeMessage message = mailSender.createMimeMessage();
//             MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
//             helper.setFrom(fromEmail);
//             helper.setTo(utilisateur.getEmail());
//             helper.setSubject("Réinitialisation de votre mot de passe");
            
//             String resetUrl = frontendUrl + "/reset-password?token=" + token;
            
//             String htmlContent = buildPasswordResetEmail(utilisateur.getNomComplet(), resetUrl);
//             helper.setText(htmlContent, true);
            
//             mailSender.send(message);
            
//         } catch (MessagingException e) {
//             throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
//         }
//     }
    
//     public void notifyAdminNewRegistration(Utilisateur utilisateur) {
//         // Récupérer tous les administrateurs (simplification)
//         String adminEmail = "[email protected]"; // À remplacer par une requête
        
//         try {
//             SimpleMailMessage message = new SimpleMailMessage();
//             message.setFrom(fromEmail);
//             message.setTo(adminEmail);
//             message.setSubject("Nouvelle inscription en attente");
            
//             String content = String.format(
//                 "Bonjour,\n\n" +
//                 "Une nouvelle inscription est en attente de validation :\n\n" +
//                 "Nom : %s\n" +
//                 "Prénom : %s\n" +
//                 "Email : %s\n" +
//                 "Date d'inscription : %s\n\n" +
//                 "Veuillez vous connecter à l'interface d'administration pour valider ce compte.\n\n" +
//                 "Cordialement,\n" +
//                 "Système d'authentification",
//                 utilisateur.getNom(),
//                 utilisateur.getPrenom(),
//                 utilisateur.getEmail(),
//                 utilisateur.getDateInscription()
//             );
            
//             message.setText(content);
//             mailSender.send(message);
            
//         } catch (Exception e) {
//             // Log l'erreur mais ne pas faire échouer l'inscription
//             System.err.println("Erreur lors de l'envoi de notification admin: " + e.getMessage());
//         }
//     }
    
//     private String buildPasswordResetEmail(String nomComplet, String resetUrl) {
//         return String.format("""
//             <html>
//             <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
//                 <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
//                     <h2 style="color: #2563eb;">Réinitialisation de votre mot de passe</h2>
                    
//                     <p>Bonjour %s,</p>
                    
//                     <p>Vous avez demandé la réinitialisation de votre mot de passe. 
//                     Cliquez sur le lien ci-dessous pour définir un nouveau mot de passe :</p>
                    
//                     <div style="text-align: center; margin: 30px 0;">
//                         <a href="%s" 
//                            style="background-color: #2563eb; color: white; padding: 12px 30px; 
//                                   text-decoration: none; border-radius: 5px; display: inline-block;">
//                             Réinitialiser mon mot de passe
//                         </a>
//                     </div>
                    
//                     <p><strong>Important :</strong> Ce lien est valable pendant 15 minutes seulement.</p>
                    
//                     <p>Si vous n'avez pas demandé cette réinitialisation, 
//                     vous pouvez ignorer cet email en toute sécurité.</p>
                    
//                     <hr style="margin: 30px 0; border: none; border-top: 1px solid #ddd;">
                    
//                     <p style="font-size: 12px; color: #666;">
//                         Si le bouton ne fonctionne pas, copiez et collez ce lien dans votre navigateur :<br>
//                         <a href="%s">%s</a>
//                     </p>
//                 </div>
//             </body>
//             </html>
//             """, nomComplet, resetUrl, resetUrl, resetUrl);
//     }
// }