package store.example.store.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import store.example.store.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour les réponses de commande
 * 
 * Cette classe contient les données d'une commande à renvoyer au client,
 * incluant les détails de la commande et les articles commandés.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    /**
     * ID de la commande
     */
    private Long id;

    /**
     * ID de l'utilisateur qui a passé la commande
     */
    private Long userId;

    /**
     * Nom de l'utilisateur
     */
    private String userName;

    /**
     * Email de l'utilisateur
     */
    private String userEmail;

    /**
     * Montant total de la commande
     */
    private BigDecimal totalAmount;

    /**
     * Statut de la commande
     */
    private String status;

    /**
     * Nom d'affichage du statut
     */
    private String statusDisplayName;

    /**
     * Adresse de livraison
     */
    private String shippingAddress;

    /**
     * Numéro de téléphone
     */
    private String phone;

    /**
     * Date de création de la commande
     */
    private LocalDateTime createdAt;

    /**
     * Date de dernière modification
     */
    private LocalDateTime updatedAt;

    /**
     * Liste des articles de la commande
     */
    private List<OrderItemResponse> orderItems;

    /**
     * Code de réduction appliqué (si applicable)
     */
    private String discountCode;

    /**
     * Montant de la réduction appliquée
     */
    private BigDecimal discountAmount;

    /**
     * Montant final après réduction
     */
    private BigDecimal finalAmount;

    /**
     * Notes spéciales de la commande
     */
    private String specialNotes;

    /**
     * DTO pour les articles de commande
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemResponse {
        
        /**
         * ID de l'article de commande
         */
        private Long id;

        /**
         * ID du produit
         */
        private Long productId;

        /**
         * Nom du produit
         */
        private String productName;

        /**
         * Image du produit
         */
        private String productImage;

        /**
         * Quantité commandée
         */
        private int quantity;

        /**
         * Prix unitaire au moment de la commande
         */
        private BigDecimal unitPrice;

        /**
         * Prix total pour cet article
         */
        private BigDecimal totalPrice;
    }

    /**
     * Crée un OrderResponse à partir d'une entité Order
     * 
     * @param order L'entité Order
     * @return Un OrderResponse
     */
    public static OrderResponse fromOrder(Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(item -> {
                    var product = item.getProduct();
                    return OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(product != null ? product.getId() : null)
                        .productName(product != null ? product.getName() : null)
                        .productImage(product != null && product.getImageUrl() != null ? product.getImageUrl() : null)
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .build();
                })
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .userName(order.getUser().getName())
                .userEmail(order.getUser().getEmail())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .statusDisplayName(order.getStatus().getDisplayName())
                .shippingAddress(order.getShippingAddress())
                .phone(order.getPhone())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .orderItems(orderItemResponses)
                .finalAmount(order.getTotalAmount()) // Par défaut, pas de réduction
                .build();
    }
}