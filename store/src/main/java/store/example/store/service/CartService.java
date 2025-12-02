package store.example.store.service;

import store.example.store.dto.request.CartItemRequest;
import store.example.store.dto.reponse.CartResponse;
import store.example.store.entity.CartItem;
import store.example.store.entity.Product;
import store.example.store.entity.User;
import store.example.store.exception.ResourceNotFoundException;
import store.example.store.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour la gestion du panier d'achat
 * 
 * Cette classe contient la logique métier pour les opérations
 * liées au panier (ajout, modification, suppression d'articles).
 */
@Service
@Transactional
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    /**
     * Récupère le panier d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @return Contenu du panier
     */
    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        List<CartResponse.CartItemResponse> itemResponses = cartItems.stream()
                .map(CartResponse.CartItemResponse::fromEntity)
                .collect(Collectors.toList());

        // Calcul des totaux
        int totalItems = itemResponses.size();
        int totalQuantity = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
        BigDecimal totalAmount = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .items(itemResponses)
                .totalItems(totalItems)
                .totalQuantity(totalQuantity)
                .totalAmount(totalAmount)
                .build();
    }

    /**
     * Ajoute un article au panier
     * 
     * @param userId  ID de l'utilisateur
     * @param request Données de l'article à ajouter
     * @return Panier mis à jour
     * @throws IllegalArgumentException Si le stock est insuffisant
     */
    public CartResponse addToCart(Long userId, CartItemRequest request) {
        User user = userService.findById(userId);
        Product product = productService.findById(request.getProductId());

        // Vérification du stock
        if (!product.hasEnoughStock(request.getQuantity())) {
            throw new IllegalArgumentException("Stock insuffisant pour le produit " + product.getName());
        }

        // Vérification si l'article existe déjà dans le panier
        Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());

        if (existingItem.isPresent()) {
            // Mise à jour de la quantité
            CartItem cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + request.getQuantity();

            // Vérification du stock pour la nouvelle quantité
            if (!product.hasEnoughStock(newQuantity)) {
                throw new IllegalArgumentException("Stock insuffisant pour la quantité demandée");
            }

            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        } else {
            // Création d'un nouvel article
            CartItem cartItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cartItemRepository.save(cartItem);
        }

        return getCart(userId);
    }

    /**
     * Met à jour la quantité d'un article dans le panier
     * 
     * @param userId      ID de l'utilisateur
     * @param cartItemId  ID de l'article dans le panier
     * @param newQuantity Nouvelle quantité
     * @return Panier mis à jour
     * @throws ResourceNotFoundException Si l'article n'existe pas
     * @throws IllegalArgumentException  Si le stock est insuffisant
     */
    public CartResponse updateCartItem(Long userId, Long cartItemId, int newQuantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Article du panier non trouvé"));

        // Vérification que l'article appartient à l'utilisateur
        if (!cartItem.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Cet article ne vous appartient pas");
        }

        // Vérification du stock
        if (!cartItem.getProduct().hasEnoughStock(newQuantity)) {
            throw new IllegalArgumentException("Stock insuffisant pour la quantité demandée");
        }

        cartItem.updateQuantity(newQuantity);
        cartItemRepository.save(cartItem);

        return getCart(userId);
    }

    /**
     * Supprime un article du panier
     * 
     * @param userId     ID de l'utilisateur
     * @param cartItemId ID de l'article à supprimer
     * @return Panier mis à jour
     * @throws ResourceNotFoundException Si l'article n'existe pas
     */
    public CartResponse removeFromCart(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Article du panier non trouvé"));

        // Vérification que l'article appartient à l'utilisateur
        if (!cartItem.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Cet article ne vous appartient pas");
        }

        cartItemRepository.delete(cartItem);
        return getCart(userId);
    }

    /**
     * Vide complètement le panier d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     */
    public void clearCart(Long userId) {
        cartItemRepository.deleteAllByUserId(userId);
    }

    /**
     * Récupère le nombre d'articles dans le panier
     * 
     * @param userId ID de l'utilisateur
     * @return Nombre d'articles
     */
    @Transactional(readOnly = true)
    public long getCartItemCount(Long userId) {
        return cartItemRepository.countByUserId(userId);
    }

    /**
     * Récupère la quantité totale d'articles dans le panier
     * 
     * @param userId ID de l'utilisateur
     * @return Quantité totale
     */
    @Transactional(readOnly = true)
    public int getTotalQuantity(Long userId) {
        return cartItemRepository.getTotalQuantityByUserId(userId);
    }

    /**
     * Vérifie si tous les articles du panier sont disponibles en stock
     * 
     * @param userId ID de l'utilisateur
     * @return true si tous les articles sont disponibles
     */
    @Transactional(readOnly = true)
    public boolean isCartValid(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        return cartItems.stream().allMatch(CartItem::isStockAvailable);
    }

    /**
     * Récupère les articles du panier pour une commande
     * 
     * @param userId ID de l'utilisateur
     * @return Liste des articles du panier
     */
    @Transactional(readOnly = true)
    public List<CartItem> getCartItemsForOrder(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }
}
