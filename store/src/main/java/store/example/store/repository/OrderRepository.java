package store.example.store.repository;


import store.example.store.entity.Order;
import store.example.store.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour l'entité Order
 * 
 * Cette interface étend JpaRepository pour fournir les opérations CRUD de base
 * et définit des méthodes de recherche personnalisées pour les commandes.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Trouve toutes les commandes d'un utilisateur, triées par date de création décroissante
     * 
     * @param user L'utilisateur
     * @return Liste des commandes de l'utilisateur
     */
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    /**
     * Trouve toutes les commandes d'un utilisateur par son ID, triées par date de création décroissante
     * 
     * @param userId L'ID de l'utilisateur
     * @return Liste des commandes de l'utilisateur
     */
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Trouve toutes les commandes d'un utilisateur avec pagination
     * 
     * @param userId L'ID de l'utilisateur
     * @param pageable Paramètres de pagination
     * @return Page des commandes de l'utilisateur
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);

    /**
     * Trouve toutes les commandes par statut
     * 
     * @param status Le statut recherché
     * @return Liste des commandes avec ce statut
     */
    List<Order> findByStatus(Order.OrderStatus status);

    /**
     * Trouve toutes les commandes créées entre deux dates
     * 
     * @param startDate Date de début
     * @param endDate Date de fin
     * @return Liste des commandes dans cette période
     */
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Trouve toutes les commandes d'un utilisateur avec un statut spécifique
     * 
     * @param userId L'ID de l'utilisateur
     * @param status Le statut recherché
     * @return Liste des commandes correspondantes
     */
    List<Order> findByUserIdAndStatus(Long userId, Order.OrderStatus status);

    /**
     * Compte le nombre de commandes d'un utilisateur
     * 
     * @param userId L'ID de l'utilisateur
     * @return Le nombre de commandes
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    /**
     * Calcule le montant total des commandes d'un utilisateur
     * 
     * @param userId L'ID de l'utilisateur
     * @return Le montant total des commandes
     */
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.user.id = :userId")
    double getTotalAmountByUserId(@Param("userId") Long userId);

    /**
     * Trouve les commandes récentes (dernières 24 heures)
     * 
     * @return Liste des commandes récentes
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt >= :since ORDER BY o.createdAt DESC")
    List<Order> findRecentOrders(@Param("since") LocalDateTime since);
}

