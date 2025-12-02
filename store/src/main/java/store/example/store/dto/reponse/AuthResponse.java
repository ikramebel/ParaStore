package store.example.store.dto.reponse;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les réponses d'authentification
 * 
 * Cette classe contient les données renvoyées après une authentification réussie,
 * incluant le token JWT et les informations de l'utilisateur.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    /**
     * Token JWT pour l'authentification
     */
    private String token;

    /**
     * Type de token (généralement "Bearer")
     */
    @Builder.Default
    private String type = "Bearer";

    /**
     * ID de l'utilisateur
     */
    private Long id;

    /**
     * Nom de l'utilisateur
     */
    private String name;

    /**
     * Email de l'utilisateur
     */
    private String email;

    /**
     * Rôle de l'utilisateur
     */
    private String role;

    /**
     * Message de succès ou d'information
     */
    private String message;
}

