package store.example.store.dto.reponse;


import store.example.store.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO pour les réponses contenant les informations d'un produit
 * 
 * Cette classe contient les données d'un produit à renvoyer au client,
 * sans exposer directement l'entité Product.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    /**
     * ID du produit
     */
    private Long id;

    /**
     * Nom du produit
     */
    private String name;

    /**
     * Description du produit
     */
    private String description;

    /**
     * Prix du produit
     */
    private BigDecimal price;

    /**
     * Catégorie du produit
     */
    private String category;

    /**
     * URL de l'image du produit
     */
    private String imageUrl;

    /**
     * Disponibilité du produit
     */
    private boolean available;

    /**
     * Quantité en stock
     */
    private int stockQuantity;

    /**
     * Date de création du produit
     */
    private LocalDateTime createdAt;

    /**
     * Indique si le produit est en stock
     */
    private boolean inStock;

    /**
     * Crée un ProductResponse à partir d'une entité Product
     * 
     * @param product L'entité Product
     * @return Un ProductResponse
     */
    public static ProductResponse fromEntity(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .available(product.isAvailable())
                .stockQuantity(product.getStockQuantity())
                .createdAt(product.getCreatedAt())
                .inStock(product.isInStock())
                .build();
    }
}
