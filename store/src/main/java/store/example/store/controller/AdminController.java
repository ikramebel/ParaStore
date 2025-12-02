package store.example.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import store.example.store.entity.User;
import store.example.store.service.UserService;
import store.example.store.service.OrderService;
import store.example.store.service.ProductService;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur pour les fonctionnalités d'administration (Admin)
 * 
 * Ce contrôleur contient les endpoints accessibles uniquement aux utilisateurs
 * ayant le rôle ADMIN.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    /**
     * Obtenir tous les utilisateurs
     * 
     * @return Liste de tous les utilisateurs
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Obtenir un utilisateur par ID
     * 
     * @param userId ID de l'utilisateur
     * @return Utilisateur trouvé
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * Mettre à jour le rôle d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @param role Nouveau rôle
     * @return Utilisateur mis à jour
     */
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<User> updateUserRole(
            @PathVariable Long userId,
            @RequestParam String role) {
        User updatedUser = userService.updateUserRole(userId, role);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Désactiver/Activer un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @param enabled Statut d'activation
     * @return Utilisateur mis à jour
     */
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<User> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam boolean enabled) {
        User updatedUser = userService.updateUserStatus(userId, enabled);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Supprimer un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @return Message de confirmation
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(Map.of("message", "Utilisateur supprimé avec succès"));
    }

    /**
     * Obtenir les statistiques générales
     * 
     * @return Statistiques de l'application
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getGeneralStatistics() {
        // Implémentation des statistiques générales
        Map<String, Object> stats = Map.of(
            "totalUsers", userService.getTotalUsersCount(),
            "totalOrders", orderService.getAllOrdersForManager().size(),
            "totalProducts", productService.getAllProducts().size(),
            "message", "Statistiques générales - À compléter"
        );
        return ResponseEntity.ok(stats);
    }

    /**
     * Obtenir les statistiques des utilisateurs par rôle
     * 
     * @return Statistiques des rôles
     */
    @GetMapping("/statistics/users-by-role")
    public ResponseEntity<?> getUserStatisticsByRole() {
        Map<String, Long> roleStats = userService.getUserCountByRole();
        return ResponseEntity.ok(roleStats);
    }

    /**
     * Supprimer un produit (Admin seulement)
     * 
     * @param productId ID du produit
     * @return Message de confirmation
     */
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(Map.of("message", "Produit supprimé avec succès"));
    }

    /**
     * Obtenir les logs d'activité (placeholder)
     * 
     * @return Logs d'activité
     */
    @GetMapping("/logs")
    public ResponseEntity<?> getActivityLogs() {
        // Implémentation des logs d'activité
        return ResponseEntity.ok(Map.of("message", "Logs d'activité - À implémenter"));
    }
}
