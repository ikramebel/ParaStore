package store.example.store.dto.request;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les requêtes de connexion
 * 
 * Cette classe contient les données nécessaires pour l'authentification d'un utilisateur.
 * Elle utilise Bean Validation pour valider les données d'entrée.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * Adresse email de l'utilisateur
     * Doit être un email valide et non vide
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    /**
     * Mot de passe de l'utilisateur
     * Ne doit pas être vide
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}