package store.example.store.repository;


import store.example.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Product
 * 
 * Cette interface étend JpaRepository pour fournir les opérations CRUD de base
 * et définit des méthodes de recherche personnalisées pour les produits.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Trouve tous les produits disponibles
     * 
     * @return Liste des produits disponibles
     */
    List<Product> findByAvailableTrue();

    /**
     * Trouve tous les produits d'une catégorie donnée
     * 
     * @param category La catégorie recherchée
     * @return Liste des produits de cette catégorie
     */
    List<Product> findByCategory(String category);

    /**
     * Trouve tous les produits disponibles d'une catégorie donnée
     * 
     * @param category La catégorie recherchée
     * @return Liste des produits disponibles de cette catégorie
     */
    List<Product> findByCategoryAndAvailableTrue(String category);

    /**
     * Trouve tous les produits dont le nom contient le texte recherché (insensible à la casse)
     * 
     * @param name Le texte à rechercher dans le nom
     * @return Liste des produits correspondants
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Trouve tous les produits disponibles dont le nom contient le texte recherché
     * 
     * @param name Le texte à rechercher dans le nom
     * @return Liste des produits disponibles correspondants
     */
    List<Product> findByNameContainingIgnoreCaseAndAvailableTrue(String name);

    /**
     * Trouve toutes les catégories distinctes des produits disponibles
     * 
     * @return Liste des catégories distinctes
     */
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.available = true")
    List<String> findDistinctCategories();

    /**
     * Trouve les produits avec un stock faible (moins de la quantité spécifiée)
     * 
     * @param threshold Seuil de stock faible
     * @return Liste des produits avec un stock faible
     */
    @Query("SELECT p FROM Product p WHERE p.available = true AND p.stockQuantity < :threshold")
    List<Product> findProductsWithLowStock(@Param("threshold") int threshold);

    /**
     * Trouve les produits en rupture de stock
     * 
     * @return Liste des produits en rupture de stock
     */
    @Query("SELECT p FROM Product p WHERE p.available = true AND p.stockQuantity = 0")
    List<Product> findOutOfStockProducts();
}
