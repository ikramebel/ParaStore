package store.example.store.dto.reponse;



import store.example.store.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour les réponses contenant les informations du panier
 * 
 * Cette classe contient les données du panier d'un utilisateur,
 * incluant les articles et le total.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {

    /**
     * Liste des articles dans le panier
     */
    private List<CartItemResponse> items;

    /**
     * Nombre total d'articles dans le panier
     */
    private int totalItems;

    /**
     * Quantité totale de tous les articles
     */
    private int totalQuantity;

    /**
     * Montant total du panier
     */
    private BigDecimal totalAmount;

    /**
     * DTO pour un article du panier
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CartItemResponse {

        /**
         * ID de l'article dans le panier
         */
        private Long id;

        /**
         * Informations du produit
         */
        private ProductResponse product;

        /**
         * Quantité de ce produit dans le panier
         */
        private int quantity;

        /**
         * Prix total pour cet article (prix unitaire × quantité)
         */
        private BigDecimal totalPrice;

        /**
         * Date d'ajout au panier
         */
        private LocalDateTime createdAt;

        /**
         * Indique si le stock est suffisant pour cette quantité
         */
        private boolean stockAvailable;

        /**
         * Crée un CartItemResponse à partir d'une entité CartItem
         * 
         * @param cartItem L'entité CartItem
         * @return Un CartItemResponse
         */
        public static CartItemResponse fromEntity(CartItem cartItem) {
            return CartItemResponse.builder()
                    .id(cartItem.getId())
                    .product(ProductResponse.fromEntity(cartItem.getProduct()))
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getTotalPrice())
                    .createdAt(cartItem.getCreatedAt())
                    .stockAvailable(cartItem.isStockAvailable())
                    .build();
        }
    }
}

