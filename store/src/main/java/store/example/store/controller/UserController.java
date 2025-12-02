package store.example.store.controller;



import store.example.store.entity.User;
import store.example.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur REST pour la gestion des utilisateurs
 * 
 * Ce contrôleur gère les endpoints liés aux utilisateurs :
 * consultation et modification du profil.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Récupère le profil de l'utilisateur connecté
     * 
     * @return Informations du profil utilisateur
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        try {
            String email = getCurrentUserEmail();
            User user = userService.findByEmail(email);
            
            // Création d'une réponse sans le mot de passe
            Map<String, Object> userProfile = Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "phone", user.getPhone() != null ? user.getPhone() : "",
                "address", user.getAddress() != null ? user.getAddress() : "",
                "role", user.getRole().name(),
                "createdAt", user.getCreatedAt()
            );
            
            return ResponseEntity.ok(userProfile);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la récupération du profil"));
        }
    }

    /**
     * Met à jour le profil de l'utilisateur connecté
     * 
     * @param request Nouvelles informations du profil
     * @return Profil mis à jour
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody Map<String, String> request) {
        try {
            String email = getCurrentUserEmail();
            User currentUser = userService.findByEmail(email);
            
            String name = request.get("name");
            String phone = request.get("phone");
            String address = request.get("address");
            
            User updatedUser = userService.updateUser(currentUser.getId(), name, phone, address);
            
            // Création d'une réponse sans le mot de passe
            Map<String, Object> userProfile = Map.of(
                "id", updatedUser.getId(),
                "name", updatedUser.getName(),
                "email", updatedUser.getEmail(),
                "phone", updatedUser.getPhone() != null ? updatedUser.getPhone() : "",
                "address", updatedUser.getAddress() != null ? updatedUser.getAddress() : "",
                "role", updatedUser.getRole().name(),
                "createdAt", updatedUser.getCreatedAt()
            );
            
            return ResponseEntity.ok(userProfile);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la mise à jour du profil"));
        }
    }

    /**
     * Change le mot de passe de l'utilisateur connecté
     * 
     * @param request Ancien et nouveau mot de passe
     * @return Message de confirmation
     */
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        try {
            String email = getCurrentUserEmail();
            User currentUser = userService.findByEmail(email);
            
            String currentPassword = request.get("currentPassword");
            String newPassword = request.get("newPassword");
            
            if (currentPassword == null || newPassword == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Mot de passe actuel et nouveau mot de passe requis"));
            }
            
            if (newPassword.length() < 6) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Le nouveau mot de passe doit contenir au moins 6 caractères"));
            }
            
            userService.changePassword(currentUser.getId(), currentPassword, newPassword);
            
            return ResponseEntity.ok(Map.of("message", "Mot de passe changé avec succès"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors du changement de mot de passe"));
        }
    }

    /**
     * Supprime le compte de l'utilisateur connecté
     * 
     * @return Message de confirmation
     */
    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteUserAccount() {
        try {
            String email = getCurrentUserEmail();
            User currentUser = userService.findByEmail(email);
            
            userService.deleteUser(currentUser.getId());
            
            return ResponseEntity.ok(Map.of("message", "Compte supprimé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la suppression du compte"));
        }
    }

    // Endpoints d'administration (nécessitent le rôle ADMIN)

    /**
     * Récupère tous les utilisateurs (pour l'administration)
     * 
     * @return Liste de tous les utilisateurs
     */
    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            var users = userService.findAllUsers().stream()
                    .map(user -> Map.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "phone", user.getPhone() != null ? user.getPhone() : "",
                        "address", user.getAddress() != null ? user.getAddress() : "",
                        "role", user.getRole().name(),
                        "createdAt", user.getCreatedAt()
                    ))
                    .toList();
            
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la récupération des utilisateurs"));
        }
    }

    /**
     * Récupère un utilisateur par son ID (pour l'administration)
     * 
     * @param userId ID de l'utilisateur
     * @return Informations de l'utilisateur
     */
    @GetMapping("/admin/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.findById(userId);
            
            Map<String, Object> userInfo = Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "phone", user.getPhone() != null ? user.getPhone() : "",
                "address", user.getAddress() != null ? user.getAddress() : "",
                "role", user.getRole().name(),
                "createdAt", user.getCreatedAt()
            );
            
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Supprime un utilisateur (pour l'administration)
     * 
     * @param userId ID de l'utilisateur à supprimer
     * @return Message de confirmation
     */
    @DeleteMapping("/admin/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(Map.of("message", "Utilisateur supprimé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la suppression de l'utilisateur"));
        }
    }

    /**
     * Méthode utilitaire pour récupérer l'email de l'utilisateur connecté
     * 
     * @return Email de l'utilisateur
     */
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
