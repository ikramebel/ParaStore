package store.example.store.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les requêtes d'ajout/modification d'articles dans le panier
 * 
 * Cette classe contient les données nécessaires pour ajouter ou modifier
 * un article dans le panier d'un utilisateur.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {

    /**
     * ID du produit à ajouter au panier
     * Ne peut pas être null
     */
    @NotNull(message = "L'ID du produit est obligatoire")
    private Long productId;

    /**
     * Quantité du produit à ajouter
     * Doit être au minimum 1
     */
    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au minimum 1")
    private Integer quantity;
}

