package store.example.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import store.example.store.entity.Product;
import store.example.store.entity.Order;
import store.example.store.service.ProductService;
import store.example.store.service.OrderService;

import java.util.List;

/**
 * Contrôleur pour les fonctionnalités de gestion (Manager)
 * 
 * Ce contrôleur contient les endpoints accessibles aux utilisateurs
 * ayant le rôle MANAGER ou ADMIN.
 */
@RestController
@RequestMapping("/api/manager")
@PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
public class ManagerController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    /**
     * Obtenir toutes les commandes (pour gestion)
     * 
     * @return Liste de toutes les commandes
     */
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrdersForManager();
        return ResponseEntity.ok(orders);
    }

    /**
     * Mettre à jour le statut d'une commande
     * 
     * @param orderId ID de la commande
     * @param status Nouveau statut
     * @return Commande mise à jour
     */
    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Ajouter un nouveau produit
     * 
     * @param product Produit à ajouter
     * @return Produit créé
     */
    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    /**
     * Mettre à jour un produit existant
     * 
     * @param productId ID du produit
     * @param product Données du produit à mettre à jour
     * @return Produit mis à jour
     */
    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(productId, product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Mettre à jour le stock d'un produit
     * 
     * @param productId ID du produit
     * @param stock Nouveau stock
     * @return Produit mis à jour
     */
    @PutMapping("/products/{productId}/stock")
    public ResponseEntity<Product> updateProductStock(
            @PathVariable Long productId,
            @RequestParam Integer stock) {
        Product updatedProduct = productService.updateProductStock(productId, stock);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Obtenir les statistiques des ventes
     * 
     * @return Statistiques des ventes
     */
    @GetMapping("/statistics/sales")
    public ResponseEntity<?> getSalesStatistics() {
        // Implémentation des statistiques de vente
        // Pour l'instant, retourne un message simple
        return ResponseEntity.ok("Statistiques des ventes - À implémenter");
    }

    /**
     * Obtenir les statistiques des produits
     * 
     * @return Statistiques des produits
     */
    @GetMapping("/statistics/products")
    public ResponseEntity<?> getProductStatistics() {
        // Implémentation des statistiques de produits
        // Pour l'instant, retourne un message simple
        return ResponseEntity.ok("Statistiques des produits - À implémenter");
    }
}