package store.example.store.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entité représentant une commande
 * 
 * Cette classe contient toutes les informations d'une commande :
 * utilisateur, montant total, statut, adresse de livraison et articles commandés.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false)
    private String shippingAddress;

    private String phone;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    /**
     * Calcule le montant total de la commande à partir des articles
     * 
     * @return Le montant total calculé
     */
    public BigDecimal calculateTotalAmount() {
        if (orderItems == null || orderItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Met à jour le statut de la commande
     * 
     * @param newStatus Nouveau statut
     */
    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Vérifie si la commande peut être annulée
     * 
     * @return true si la commande peut être annulée
     */
    public boolean canBeCancelled() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
    }

    /**
     * Annule la commande si possible
     * 
     * @throws IllegalStateException si la commande ne peut pas être annulée
     */
    public void cancel() {
        if (!canBeCancelled()) {
            throw new IllegalStateException("La commande ne peut pas être annulée dans son état actuel : " + status);
        }
        updateStatus(OrderStatus.CANCELLED);
    }

    /**
     * Énumération des statuts de commande
     */
    public enum OrderStatus {
        PENDING("En attente"),
        CONFIRMED("Confirmée"),
        SHIPPED("Expédiée"),
        DELIVERED("Livrée"),
        CANCELLED("Annulée");

        private final String displayName;

        OrderStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
