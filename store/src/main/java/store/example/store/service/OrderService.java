package store.example.store.service;

import store.example.store.dto.request.OrderRequest;
import store.example.store.dto.reponse.OrderResponse;
import store.example.store.entity.*;
import store.example.store.exception.ResourceNotFoundException;
import store.example.store.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des commandes
 * 
 * Cette classe contient la logique métier pour les opérations
 * liées aux commandes (création, suivi, gestion des statuts).
 */
@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    /**
     * Crée une nouvelle commande à partir du panier de l'utilisateur
     * 
     * @param userId       ID de l'utilisateur
     * @param orderRequest Données de la commande
     * @return Commande créée
     * @throws IllegalArgumentException Si le panier est vide ou invalide
     */
    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {
        User user = userService.findById(userId);
        List<CartItem> cartItems = cartService.getCartItemsForOrder(userId);

        // Vérification que le panier n'est pas vide
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Le panier est vide");
        }

        // Vérification de la disponibilité des stocks
        if (!cartService.isCartValid(userId)) {
            throw new IllegalArgumentException("Certains articles ne sont plus disponibles en stock");
        }

        // Calcul du montant total
        BigDecimal totalAmount = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Création de la commande
        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .status(Order.OrderStatus.PENDING)
                .shippingAddress(orderRequest.getShippingAddress())
                .phone(orderRequest.getPhone())
                .build();

        Order savedOrder = orderRepository.save(order);

        // Création des articles de commande
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> OrderItem.fromCartItem(cartItem, savedOrder))
                .collect(Collectors.toList());

        savedOrder.setOrderItems(orderItems);

        // Réduction des stocks
        for (CartItem cartItem : cartItems) {
            productService.reduceStock(cartItem.getProduct().getId(), cartItem.getQuantity());
        }

        // Vidage du panier
        cartService.clearCart(userId);

        // Sauvegarde finale
        Order finalOrder = orderRepository.save(savedOrder);
        return OrderResponse.fromOrder(finalOrder);
    }

    /**
     * Récupère une commande par son ID
     * 
     * @param orderId ID de la commande
     * @param userId  ID de l'utilisateur (pour vérifier la propriété)
     * @return Commande trouvée
     * @throws ResourceNotFoundException Si la commande n'existe pas
     * @throws IllegalArgumentException  Si la commande ne appartient pas à
     *                                   l'utilisateur
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée"));

        // Vérification que la commande appartient à l'utilisateur
        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Cette commande ne vous appartient pas");
        }

        return OrderResponse.fromOrder(order);
    }

    /**
     * Récupère toutes les commandes d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @return Liste des commandes
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(OrderResponse::fromOrder)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les commandes d'un utilisateur avec pagination
     * 
     * @param userId ID de l'utilisateur
     * @param page   Numéro de page (0-based)
     * @param size   Taille de la page
     * @return Page des commandes
     */
    @Transactional(readOnly = true)
    public Page<OrderResponse> getUserOrdersWithPagination(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);

        return orderPage.map(OrderResponse::fromOrder);
    }

    /**
     * Met à jour le statut d'une commande (pour l'administration)
     * 
     * @param orderId   ID de la commande
     * @param newStatus Nouveau statut
     * @return Commande mise à jour
     */
    public OrderResponse updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée"));

        order.updateStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        return OrderResponse.fromOrder(updatedOrder);
    }

    /**
     * Annule une commande
     * 
     * @param orderId ID de la commande
     * @param userId  ID de l'utilisateur
     * @return Commande annulée
     * @throws IllegalStateException Si la commande ne peut pas être annulée
     */
    public OrderResponse cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée"));

        // Vérification que la commande appartient à l'utilisateur
        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Cette commande ne vous appartient pas");
        }

        // Annulation de la commande
        order.cancel();

        // Restauration des stocks
        for (OrderItem orderItem : order.getOrderItems()) {
            productService.increaseStock(orderItem.getProduct().getId(), orderItem.getQuantity());
        }

        Order cancelledOrder = orderRepository.save(order);
        return OrderResponse.fromOrder(cancelledOrder);
    }

    /**
     * Récupère toutes les commandes (pour l'administration)
     * 
     * @return Liste de toutes les commandes
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrdersResponse() {
        List<Order> orders = orderRepository.findAll(Sort.by("createdAt").descending());
        return orders.stream()
                .map(OrderResponse::fromOrder)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les commandes par statut
     * 
     * @param status Statut recherché
     * @return Liste des commandes avec ce statut
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(Order.OrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream()
                .map(OrderResponse::fromOrder)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les commandes récentes (dernières 24 heures)
     * 
     * @return Liste des commandes récentes
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getRecentOrders() {
        LocalDateTime since = LocalDateTime.now().minusDays(1);
        List<Order> orders = orderRepository.findRecentOrders(since);
        return orders.stream()
                .map(OrderResponse::fromOrder)
                .collect(Collectors.toList());
    }

    /**
     * Calcule le nombre total de commandes d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @return Nombre de commandes
     */
    @Transactional(readOnly = true)
    public long getUserOrderCount(Long userId) {
        return orderRepository.countByUserId(userId);
    }

    /**
     * Calcule le montant total des commandes d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @return Montant total
     */
    @Transactional(readOnly = true)
    public double getUserTotalAmount(Long userId) {
        return orderRepository.getTotalAmountByUserId(userId);
    }

    /**
     * Récupère toutes les commandes (version entité pour Manager)
     * 
     * @return Liste de toutes les commandes
     */
    @Transactional(readOnly = true)
    public List<Order> getAllOrdersForManager() {
        return orderRepository.findAll(Sort.by("createdAt").descending());
    }

    /**
     * Met à jour le statut d'une commande (version string pour Manager)
     * 
     * @param orderId ID de la commande
     * @param status Nouveau statut (string)
     * @return Commande mise à jour
     */
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée"));

        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            order.updateStatus(orderStatus);
            return orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut invalide: " + status);
        }
    }
}