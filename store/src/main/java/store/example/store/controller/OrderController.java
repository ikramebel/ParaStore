package store.example.store.controller;

import store.example.store.dto.request.OrderRequest;
import store.example.store.dto.reponse.OrderResponse;
import store.example.store.entity.Order;
import store.example.store.entity.User;
import store.example.store.service.OrderService;
import store.example.store.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des commandes
 * 
 * Ce contrôleur gère les endpoints liés aux commandes :
 * création, consultation, suivi et annulation.
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * Crée une nouvelle commande à partir du panier
     * 
     * @param orderRequest Données de la commande
     * @return Commande créée
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        try {
            Long userId = getCurrentUserId();
            OrderResponse order = orderService.createOrder(userId, orderRequest);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la création de la commande"));
        }
    }

    /**
     * Récupère toutes les commandes de l'utilisateur connecté
     * 
     * @return Liste des commandes
     */
    @GetMapping
    public ResponseEntity<?> getUserOrders() {
        try {
            Long userId = getCurrentUserId();
            List<OrderResponse> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la récupération des commandes"));
        }
    }

    /**
     * Récupère les commandes avec pagination
     * 
     * @param page Numéro de page (0-based)
     * @param size Taille de la page
     * @return Page des commandes
     */
    @GetMapping("/paginated")
    public ResponseEntity<?> getUserOrdersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Long userId = getCurrentUserId();
            Page<OrderResponse> orderPage = orderService.getUserOrdersWithPagination(userId, page, size);
            return ResponseEntity.ok(orderPage);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la récupération des commandes"));
        }
    }

    /**
     * Récupère une commande spécifique par son ID
     * 
     * @param orderId ID de la commande
     * @return Commande trouvée
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        try {
            Long userId = getCurrentUserId();
            OrderResponse order = orderService.getOrder(orderId, userId);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Annule une commande
     * 
     * @param orderId ID de la commande à annuler
     * @return Commande annulée
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try {
            Long userId = getCurrentUserId();
            OrderResponse order = orderService.cancelOrder(orderId, userId);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de l'annulation de la commande"));
        }
    }

    /**
     * Récupère les statistiques des commandes de l'utilisateur
     * 
     * @return Statistiques des commandes
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getUserOrderStats() {
        try {
            Long userId = getCurrentUserId();
            long orderCount = orderService.getUserOrderCount(userId);
            double totalAmount = orderService.getUserTotalAmount(userId);

            return ResponseEntity.ok(Map.of(
                    "totalOrders", orderCount,
                    "totalAmount", totalAmount));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la récupération des statistiques"));
        }
    }

    // Endpoints d'administration (nécessitent le rôle ADMIN)

    /**
     * Récupère toutes les commandes (pour l'administration)
     * 
     * @return Liste de toutes les commandes
     */
    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllOrders() {
        try {
            List<OrderResponse> orders = orderService.getAllOrdersResponse();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la récupération des commandes"));
        }
    }

    /**
     * Met à jour le statut d'une commande (pour l'administration)
     * 
     * @param orderId ID de la commande
     * @param request Nouveau statut
     * @return Commande mise à jour
     */
    @PutMapping("/admin/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId,
            @RequestBody Map<String, String> request) {
        try {
            String statusString = request.get("status");
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(statusString.toUpperCase());

            OrderResponse order = orderService.updateOrderStatus(orderId, newStatus);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Statut invalide"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la mise à jour du statut"));
        }
    }

    /**
     * Récupère les commandes par statut (pour l'administration)
     * 
     * @param status Statut recherché
     * @return Liste des commandes avec ce statut
     */
    @GetMapping("/admin/status/{status}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable String status) {
        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            List<OrderResponse> orders = orderService.getOrdersByStatus(orderStatus);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Statut invalide"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la récupération des commandes"));
        }
    }

    /**
     * Récupère les commandes récentes (pour l'administration)
     * 
     * @return Liste des commandes récentes
     */
    @GetMapping("/admin/recent")
    public ResponseEntity<?> getRecentOrders() {
        try {
            List<OrderResponse> orders = orderService.getRecentOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la récupération des commandes récentes"));
        }
    }

    /**
     * Méthode utilitaire pour récupérer l'ID de l'utilisateur connecté
     * 
     * @return ID de l'utilisateur
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        try {
            // Récupérer l'utilisateur depuis la base de données par son email
            User user = userService.findByEmail(email);
            return user.getId(); // Retourner le vrai ID de l'utilisateur
        } catch (Exception e) {
            throw new RuntimeException("Impossible de récupérer l'utilisateur connecté: " + email, e);
        }
    }
}