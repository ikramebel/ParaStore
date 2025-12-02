package store.example.store.service;

import store.example.store.dto.reponse.ProductResponse;
import store.example.store.entity.Product;
import store.example.store.exception.ResourceNotFoundException;
import store.example.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des produits
 * 
 * Cette classe contient la logique métier pour les opérations
 * liées aux produits (recherche, filtrage, gestion du stock).
 */
@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Récupère tous les produits disponibles
     * 
     * @return Liste des produits disponibles
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllAvailableProducts() {
        return productRepository.findByAvailableTrue()
                .stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Récupère tous les produits (pour l'administration)
     * 
     * @return Liste de tous les produits
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Trouve un produit par son ID
     * 
     * @param id ID du produit
     * @return Produit trouvé
     * @throws ResourceNotFoundException Si le produit n'existe pas
     */
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'ID : " + id));
    }

    /**
     * Récupère un produit par son ID (version DTO)
     * 
     * @param id ID du produit
     * @return ProductResponse
     */
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = findById(id);
        return ProductResponse.fromEntity(product);
    }

    /**
     * Récupère les produits par catégorie
     * 
     * @param category Catégorie recherchée
     * @return Liste des produits de cette catégorie
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndAvailableTrue(category)
                .stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Recherche des produits par nom
     * 
     * @param name Nom ou partie du nom à rechercher
     * @return Liste des produits correspondants
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndAvailableTrue(name)
                .stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Récupère toutes les catégories disponibles
     * 
     * @return Liste des catégories distinctes
     */
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }

    /**
     * Crée un nouveau produit (pour l'administration)
     * 
     * @param name          Nom du produit
     * @param description   Description du produit
     * @param price         Prix du produit
     * @param category      Catégorie du produit
     * @param imageUrl      URL de l'image
     * @param stockQuantity Quantité en stock
     * @return Produit créé
     */
    public ProductResponse createProduct(String name, String description, BigDecimal price,
            String category, String imageUrl, int stockQuantity) {
        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .category(category)
                .imageUrl(imageUrl)
                .available(true)
                .stockQuantity(stockQuantity)
                .build();

        Product savedProduct = productRepository.save(product);
        return ProductResponse.fromEntity(savedProduct);
    }

    /**
     * Met à jour un produit existant
     * 
     * @param id            ID du produit
     * @param name          Nouveau nom
     * @param description   Nouvelle description
     * @param price         Nouveau prix
     * @param category      Nouvelle catégorie
     * @param imageUrl      Nouvelle URL d'image
     * @param stockQuantity Nouvelle quantité en stock
     * @param available     Nouvelle disponibilité
     * @return Produit mis à jour
     */
    public ProductResponse updateProduct(Long id, String name, String description, BigDecimal price,
            String category, String imageUrl, Integer stockQuantity, Boolean available) {
        Product product = findById(id);

        if (name != null && !name.trim().isEmpty()) {
            product.setName(name.trim());
        }
        if (description != null) {
            product.setDescription(description.trim());
        }
        if (price != null) {
            product.setPrice(price);
        }
        if (category != null && !category.trim().isEmpty()) {
            product.setCategory(category.trim());
        }
        if (imageUrl != null) {
            product.setImageUrl(imageUrl.trim());
        }
        if (stockQuantity != null) {
            product.setStockQuantity(stockQuantity);
        }
        if (available != null) {
            product.setAvailable(available);
        }

        Product updatedProduct = productRepository.save(product);
        return ProductResponse.fromEntity(updatedProduct);
    }

    /**
     * Supprime un produit
     * 
     * @param id ID du produit à supprimer
     */
    public void deleteProduct(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
    }

    /**
     * Vérifie si un produit a suffisamment de stock
     * 
     * @param productId ID du produit
     * @param quantity  Quantité demandée
     * @return true si le stock est suffisant
     */
    @Transactional(readOnly = true)
    public boolean hasEnoughStock(Long productId, int quantity) {
        Product product = findById(productId);
        return product.hasEnoughStock(quantity);
    }

    /**
     * Réduit le stock d'un produit
     * 
     * @param productId ID du produit
     * @param quantity  Quantité à déduire
     * @throws IllegalArgumentException Si le stock est insuffisant
     */
    public void reduceStock(Long productId, int quantity) {
        Product product = findById(productId);
        product.reduceStock(quantity);
        productRepository.save(product);
    }

    /**
     * Augmente le stock d'un produit
     * 
     * @param productId ID du produit
     * @param quantity  Quantité à ajouter
     */
    public void increaseStock(Long productId, int quantity) {
        Product product = findById(productId);
        product.increaseStock(quantity);
        productRepository.save(product);
    }

    /**
     * Récupère les produits avec un stock faible
     * 
     * @param threshold Seuil de stock faible
     * @return Liste des produits avec un stock faible
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsWithLowStock(int threshold) {
        return productRepository.findProductsWithLowStock(threshold)
                .stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les produits en rupture de stock
     * 
     * @return Liste des produits en rupture de stock
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getOutOfStockProducts() {
        return productRepository.findOutOfStockProducts()
                .stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Crée un nouveau produit (version simplifiée pour Manager)
     * 
     * @param product Produit à créer
     * @return Produit créé
     */
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Met à jour un produit existant (version simplifiée pour Manager)
     * 
     * @param productId ID du produit
     * @param product Données du produit à mettre à jour
     * @return Produit mis à jour
     */
    public Product updateProduct(Long productId, Product product) {
        Product existingProduct = findById(productId);
        
        if (product.getName() != null) {
            existingProduct.setName(product.getName());
        }
        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }
        if (product.getPrice() != null) {
            existingProduct.setPrice(product.getPrice());
        }
        if (product.getCategory() != null) {
            existingProduct.setCategory(product.getCategory());
        }
        if (product.getImageUrl() != null) {
            existingProduct.setImageUrl(product.getImageUrl());
        }
        if (product.getStockQuantity() != 0) {
            existingProduct.setStockQuantity(product.getStockQuantity());
        }
        if (product.isAvailable() != existingProduct.isAvailable()) {
            existingProduct.setAvailable(product.isAvailable());
        }
        
        return productRepository.save(existingProduct);
    }

    /**
     * Met à jour le stock d'un produit (pour Manager)
     * 
     * @param productId ID du produit
     * @param stock Nouveau stock
     * @return Produit mis à jour
     */
    public Product updateProductStock(Long productId, Integer stock) {
        Product product = findById(productId);
        product.setStockQuantity(stock);
        return productRepository.save(product);
    }
}