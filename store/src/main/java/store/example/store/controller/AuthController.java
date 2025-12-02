package store.example.store.controller;

import store.example.store.dto.request.LoginRequest;
import store.example.store.dto.request.RegisterRequest;
import store.example.store.dto.reponse.AuthResponse;
import store.example.store.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

/**
 * Contrôleur REST pour l'authentification
 * 
 * Ce contrôleur gère les endpoints liés à l'authentification :
 * inscription, connexion, déconnexion et récupération du profil utilisateur.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint pour l'inscription d'un nouvel utilisateur
     * 
     * @param registerRequest Données d'inscription
     * @return Réponse d'authentification avec le token JWT
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse authResponse = authService.registerUser(registerRequest);
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de l'inscription"));
        }
    }

    /**
     * Endpoint pour la connexion d'un utilisateur existant
     *
     * @param loginRequest Données de connexion
     * @return Réponse d'authentification avec le token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la connexion"));
        }
    }

    /**
     * Endpoint pour la déconnexion d'un utilisateur
     *
     * @return Message de confirmation de déconnexion
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        try {
            // La déconnexion côté serveur est principalement pour les logs ou la révocation de tokens si implémenté
            // Avec JWT, la gestion de la déconnexion est souvent côté client (suppression du token)
            // Ici, nous allons simplement effacer le contexte de sécurité si un utilisateur est authentifié
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            authService.logoutUser(email);
            return ResponseEntity.ok(Map.of("message", "Déconnexion réussie pour l'utilisateur: " + email));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la déconnexion"));
        }
    }

    /**
     * Endpoint pour valider un token JWT
     *
     * @param request Body contenant le token à valider
     * @return Réponse indiquant si le token est valide
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            boolean isValid = authService.validateToken(token);
            return ResponseEntity.ok(Map.of("valid", isValid));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("valid", false, "error", "Erreur lors de la validation du token"));
        }
    }
}