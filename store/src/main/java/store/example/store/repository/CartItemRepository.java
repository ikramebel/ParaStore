package store.example.store.repository;



import store.example.store.entity.CartItem;
import store.example.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité CartItem
 * 
 * Cette interface étend JpaRepository pour fournir les opérations CRUD de base
 * et définit des méthodes de recherche personnalisées pour les articles du panier.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Trouve tous les articles du panier d'un utilisateur
     * 
     * @param user L'utilisateur
     * @return Liste des articles du panier
     */
    List<CartItem> findByUser(User user);

    /**
     * Trouve tous les articles du panier d'un utilisateur par son ID
     * 
     * @param userId L'ID de l'utilisateur
     * @return Liste des articles du panier
     */
    List<CartItem> findByUserId(Long userId);

    /**
     * Trouve un article spécifique dans le panier d'un utilisateur
     * 
     * @param userId L'ID de l'utilisateur
     * @param productId L'ID du produit
     * @return Un Optional contenant l'article s'il existe
     */
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

    /**
     * Compte le nombre d'articles dans le panier d'un utilisateur
     * 
     * @param userId L'ID de l'utilisateur
     * @return Le nombre d'articles dans le panier
     */
    @Query("SELECT COUNT(c) FROM CartItem c WHERE c.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    /**
     * Calcule la quantité totale d'articles dans le panier d'un utilisateur
     * 
     * @param userId L'ID de l'utilisateur
     * @return La quantité totale d'articles
     */
    @Query("SELECT COALESCE(SUM(c.quantity), 0) FROM CartItem c WHERE c.user.id = :userId")
    int getTotalQuantityByUserId(@Param("userId") Long userId);

    /**
     * Supprime tous les articles du panier d'un utilisateur
     * 
     * @param userId L'ID de l'utilisateur
     */
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    /**
     * Supprime un article spécifique du panier d'un utilisateur
     * 
     * @param userId L'ID de l'utilisateur
     * @param productId L'ID du produit
     */
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user.id = :userId AND c.product.id = :productId")
    void deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}
