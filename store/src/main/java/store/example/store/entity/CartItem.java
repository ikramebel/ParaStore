package store.example.store.entity;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité représentant un article dans le panier d'un utilisateur
 * 
 * Cette classe fait le lien entre un utilisateur et un produit,
 * en stockant la quantité souhaitée.
 */
@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Calcule le prix total pour cet article (prix unitaire × quantité)
     * 
     * @return Le prix total de l'article
     */
    public BigDecimal getTotalPrice() {
        if (product != null && product.getPrice() != null) {
            return product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }

    /**
     * Vérifie si la quantité demandée est disponible en stock
     * 
     * @return true si le stock est suffisant
     */
    public boolean isStockAvailable() {
        return product != null && product.hasEnoughStock(quantity);
    }

    /**
     * Met à jour la quantité de l'article
     * 
     * @param newQuantity Nouvelle quantité
     * @throws IllegalArgumentException si la quantité est négative ou nulle
     */
    public void updateQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        this.quantity = newQuantity;
    }
}
