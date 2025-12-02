package store.example.store.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les requêtes d'inscription
 * 
 * Cette classe contient les données nécessaires pour créer un nouveau compte utilisateur.
 * Elle utilise Bean Validation pour valider les données d'entrée.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    /**
     * Nom complet de l'utilisateur
     * Doit contenir au moins 2 caractères
     */
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String name;

    /**
     * Adresse email de l'utilisateur
     * Doit être un email valide et non vide
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    /**
     * Mot de passe de l'utilisateur
     * Doit contenir au moins 6 caractères
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    /**
     * Numéro de téléphone de l'utilisateur (optionnel)
     */
    private String phone;

    /**
     * Adresse de l'utilisateur (optionnel)
     */
    private String address;
}
