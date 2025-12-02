package store.example.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour les demandes de création de commande
 * 
 * Cette classe contient les données nécessaires pour créer une nouvelle commande,
 * incluant l'adresse de livraison, le téléphone et les articles commandés.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    /**
     * Adresse de livraison
     */
    @NotBlank(message = "L'adresse de livraison est obligatoire")
    private String shippingAddress;

    /**
     * Numéro de téléphone pour la livraison
     */
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]{8,20}$", 
             message = "Le numéro de téléphone doit être valide")
    private String phone;

    /**
     * Liste des IDs des articles du panier à commander
     * Si null ou vide, tous les articles du panier seront commandés
     */
    private List<Long> cartItemIds;

    /**
     * Code de réduction (optionnel)
     */
    private String discountCode;

    /**
     * Notes spéciales pour la commande (optionnel)
     */
    private String specialNotes;
}