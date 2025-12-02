package store.example.store.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité représentant un produit de parapharmacie
 * 
 * Cette classe contient toutes les informations d'un produit :
 * nom, description, prix, catégorie, image, disponibilité et stock.
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private String category;

    private String imageUrl;

    @Builder.Default
    private boolean available = true;

    @Builder.Default
    private int stockQuantity = 100;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Vérifie si le produit est en stock
     * 
     * @return true si le produit est disponible et en stock
     */
    public boolean isInStock() {
        return available && stockQuantity > 0;
    }

    /**
     * Vérifie si la quantité demandée est disponible
     * 
     * @param quantity Quantité demandée
     * @return true si la quantité est disponible
     */
    public boolean hasEnoughStock(int quantity) {
        return isInStock() && stockQuantity >= quantity;
    }

    /**
     * Réduit le stock du produit
     * 
     * @param quantity Quantité à déduire
     * @throws IllegalArgumentException si la quantité est supérieure au stock
     */
    public void reduceStock(int quantity) {
        if (!hasEnoughStock(quantity)) {
            throw new IllegalArgumentException("Stock insuffisant pour le produit " + name);
        }
        this.stockQuantity -= quantity;
    }

    /**
     * Augmente le stock du produit
     * 
     * @param quantity Quantité à ajouter
     */
    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }
}
