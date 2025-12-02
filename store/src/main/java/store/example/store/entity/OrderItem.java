package store.example.store.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entité représentant un article dans une commande
 * 
 * Cette classe stocke les détails d'un produit au moment de la commande,
 * incluant le prix et la quantité commandée.
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * Calcule le prix total pour cet article (prix unitaire × quantité)
     * 
     * @return Le prix total de l'article
     */
    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Crée un OrderItem à partir d'un CartItem
     * 
     * @param cartItem L'article du panier
     * @param order La commande associée
     * @return Un nouvel OrderItem
     */
    public static OrderItem fromCartItem(CartItem cartItem, Order order) {
        return OrderItem.builder()
                .order(order)
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getProduct().getPrice())
                .build();
    }
}

