package store.example.store.controller;

import store.example.store.dto.request.CartItemRequest;
import store.example.store.dto.reponse.CartResponse;
import store.example.store.entity.User;
import store.example.store.service.CartService;
import store.example.store.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur REST pour la gestion du panier d'achat
 * 
 * Ce contrôleur gère les endpoints liés au panier :
 * consultation, ajout, modification et suppression d'articles.
 */
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    /**
     * Récupère le contenu du panier de l'utilisateur connecté
     * 
     * @return Contenu du panier
     */
    @GetMapping
    public ResponseEntity<?> getCart() {
        try {
            Long userId = getCurrentUserId();
            CartResponse cart = cartService.getCart(userId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la récupération du panier"));
        }
    }

    /**
     * Ajoute un article au panier
     * 
     * @param request Données de l'article à ajouter
     * @return Panier mis à jour
     */
    @PostMapping("/items")
    public ResponseEntity<?> addToCart(@Valid @RequestBody CartItemRequest request) {
        try {
            Long userId = getCurrentUserId();
            CartResponse cart = cartService.addToCart(userId, request);
            return ResponseEntity.ok(cart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de l'ajout au panier"));
        }
    }

    /**
     * Met à jour la quantité d'un article dans le panier
     * 
     * @param itemId  ID de l'article dans le panier
     * @param request Nouvelle quantité
     * @return Panier mis à jour
     */
    @PutMapping("/items/{itemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long itemId,
            @Valid @RequestBody CartItemRequest request) {
        try {
            Long userId = getCurrentUserId();
            CartResponse cart = cartService.updateCartItem(userId, itemId, request.getQuantity());
            return ResponseEntity.ok(cart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la mise à jour du panier"));
        }
    }

    /**
     * Supprime un article du panier
     * 
     * @param itemId ID de l'article à supprimer
     * @return Panier mis à jour
     */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long itemId) {
        try {
            Long userId = getCurrentUserId();
            CartResponse cart = cartService.removeFromCart(userId, itemId);
            return ResponseEntity.ok(cart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la suppression de l'article"));
        }
    }

    /**
     * Vide complètement le panier
     * 
     * @return Message de confirmation
     */
    @DeleteMapping
    public ResponseEntity<?> clearCart() {
        try {
            Long userId = getCurrentUserId();
            cartService.clearCart(userId);
            return ResponseEntity.ok(Map.of("message", "Panier vidé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors du vidage du panier"));
        }
    }

    /**
     * Récupère le nombre d'articles dans le panier
     * 
     * @return Nombre d'articles
     */
    @GetMapping("/count")
    public ResponseEntity<?> getCartItemCount() {
        try {
            Long userId = getCurrentUserId();
            long count = cartService.getCartItemCount(userId);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors du comptage des articles"));
        }
    }

    /**
     * Récupère la quantité totale d'articles dans le panier
     * 
     * @return Quantité totale
     */
    @GetMapping("/quantity")
    public ResponseEntity<?> getTotalQuantity() {
        try {
            Long userId = getCurrentUserId();
            int totalQuantity = cartService.getTotalQuantity(userId);
            return ResponseEntity.ok(Map.of("totalQuantity", totalQuantity));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors du calcul de la quantité totale"));
        }
    }

    /**
     * Vérifie si le panier est valide (tous les articles sont disponibles)
     * 
     * @return Statut de validité du panier
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateCart() {
        try {
            Long userId = getCurrentUserId();
            boolean isValid = cartService.isCartValid(userId);
            return ResponseEntity.ok(Map.of(
                    "valid", isValid,
                    "message", isValid ? "Panier valide" : "Certains articles ne sont plus disponibles"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la validation du panier"));
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