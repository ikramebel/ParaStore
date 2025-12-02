package store.example.store.service;

import store.example.store.dto.request.LoginRequest;
import store.example.store.dto.request.RegisterRequest;
import store.example.store.dto.reponse.AuthResponse;
import store.example.store.entity.User;
import store.example.store.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service pour l'authentification et l'autorisation
 * 
 * Cette classe gère les opérations de connexion, inscription
 * et génération de tokens JWT.
 */
@Service
@Transactional
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /**
     * Authentifie un utilisateur et génère un token JWT
     * 
     * @param loginRequest Données de connexion
     * @return Réponse d'authentification avec le token
     * @throws AuthenticationException Si les identifiants sont incorrects
     */
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        try {
            // Authentification avec Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            // Définition de l'authentification dans le contexte de sécurité
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Génération du token JWT
            String jwt = tokenProvider.generateToken(authentication);

            // Récupération des informations utilisateur
            User user = userService.findByEmail(loginRequest.getEmail());

            return AuthResponse.builder()
                    .token(jwt)
                    .type("Bearer")
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .message("Connexion réussie")
                    .build();

        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        } catch (Exception e) { // AJOUTEZ OU MODIFIEZ CE BLOC
            // Log la pile d'appels complète pour le diagnostic
            e.printStackTrace(); 
            // Relancez une RuntimeException avec le message original pour le client
            throw new RuntimeException("Erreur lors de la connexion : " + e.getMessage(), e);
        }
    }

    /**
     * Inscrit un nouvel utilisateur
     * 
     * @param registerRequest Données d'inscription
     * @return Réponse d'authentification avec le token
     * @throws IllegalArgumentException Si l'email existe déjà
     */
    public AuthResponse registerUser(RegisterRequest registerRequest) {
    try {
        // Vérification de l'unicité de l'email
        if (userService.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Un compte avec cet email existe déjà");
        }

        // Création de l'utilisateur
        User user = userService.createUser(registerRequest);

        // Génération du token JWT
        String jwt = tokenProvider.generateTokenFromUsername(user.getEmail());

        return AuthResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .message("Inscription réussie")
                .build();

    } catch (Exception e) {
        // Log complet dans la console
        e.printStackTrace();

        // Optionnel : Renvoyer un message clair dans Postman
        throw new RuntimeException("Erreur lors de l'inscription : " + e.getMessage());
    }
}

    /**
     * Récupère les informations de l'utilisateur connecté
     * 
     * @param email Email de l'utilisateur
     * @return Informations de l'utilisateur
     */
    @Transactional(readOnly = true)
    public AuthResponse getCurrentUser(String email) {
        User user = userService.findByEmail(email);

        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Informations utilisateur récupérées")
                .build();
    }

    /**
     * Valide un token JWT
     * 
     * @param token Token à valider
     * @return true si le token est valide
     */
    public boolean validateToken(String token) {
        return tokenProvider.validateToken(token);
    }

    /**
     * Extrait l'email d'un token JWT
     * 
     * @param token Token JWT
     * @return Email de l'utilisateur
     */
    public String getEmailFromToken(String token) {
        return tokenProvider.getUsernameFromJWT(token);
    }

    /**
     * Vérifie si un utilisateur a le rôle administrateur
     * 
     * @param email Email de l'utilisateur
     * @return true si l'utilisateur est administrateur
     */
    @Transactional(readOnly = true)
    public boolean isAdmin(String email) {
        User user = userService.findByEmail(email);
        return user.getRole() == User.Role.ADMIN;
    }

    /**
     * Déconnecte un utilisateur (côté serveur, principalement pour les logs)
     * 
     * @param email Email de l'utilisateur
     * @return Message de confirmation
     */
    public String logoutUser(String email) {
        // Suppression de l'authentification du contexte de sécurité
        SecurityContextHolder.clearContext();

        // Note : Avec JWT, la déconnexion est principalement côté client
        // (suppression du token du stockage local/session)

        return "Déconnexion réussie pour l'utilisateur : " + email;
    }
}