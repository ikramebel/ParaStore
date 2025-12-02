package store.example.store.controller;

import store.example.store.dto.reponse.ProductResponse;
import store.example.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des produits
 * 
 * Ce contrôleur gère les endpoints liés aux produits :
 * consultation, recherche, filtrage par catégorie.
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Récupère tous les produits disponibles
     * 
     * @return Liste des produits disponibles
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        try {
            List<ProductResponse> products = productService.getAllAvailableProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère un produit par son ID
     * 
     * @param id ID du produit
     * @return Produit trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            ProductResponse product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère les produits par catégorie
     * 
     * @param category Catégorie recherchée
     * @return Liste des produits de cette catégorie
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        try {
            List<ProductResponse> products = productService.getProductsByCategory(category);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Recherche des produits par nom
     * 
     * @param name Nom ou partie du nom à rechercher
     * @return Liste des produits correspondants
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String name) {
        try {
            List<ProductResponse> products = productService.searchProductsByName(name);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère toutes les catégories disponibles
     * 
     * @return Liste des catégories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        try {
            List<String> categories = productService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère les produits avec un stock faible (pour l'administration)
     * 
     * @param threshold Seuil de stock faible (par défaut 10)
     * @return Liste des produits avec un stock faible
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponse>> getProductsWithLowStock(
            @RequestParam(defaultValue = "10") int threshold) {
        try {
            List<ProductResponse> products = productService.getProductsWithLowStock(threshold);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère les produits en rupture de stock (pour l'administration)
     * 
     * @return Liste des produits en rupture de stock
     */
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<ProductResponse>> getOutOfStockProducts() {
        try {
            List<ProductResponse> products = productService.getOutOfStockProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Vérifie la disponibilité d'un produit
     * 
     * @param id       ID du produit
     * @param quantity Quantité demandée
     * @return Statut de disponibilité
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<?> checkProductAvailability(@PathVariable Long id,
            @RequestParam int quantity) {
        try {
            boolean available = productService.hasEnoughStock(id, quantity);
            return ResponseEntity.ok(Map.of(
                    "productId", id,
                    "requestedQuantity", quantity,
                    "available", available));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Produit non trouvé"));
        }
    }

    /**
     * Endpoint de test pour vérifier que l'API fonctionne
     * 
     * @return Message de test
     */
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "L'API des produits fonctionne correctement",
                "timestamp", System.currentTimeMillis()));
    }
}
