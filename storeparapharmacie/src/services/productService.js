import api from '../config/api';

class ProductService {
  // Récupérer tous les produits disponibles
  async getAllProducts() {
    try {
      const response = await api.get('/products');
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Récupérer un produit par son ID
  async getProductById(id) {
    try {
      const response = await api.get(`/products/${id}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Récupérer les produits par catégorie
  async getProductsByCategory(category) {
    try {
      const response = await api.get(`/products/category/${encodeURIComponent(category)}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Rechercher des produits par nom
  async searchProducts(searchTerm) {
    try {
      const response = await api.get(`/products/search`, {
        params: { name: searchTerm }
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Récupérer toutes les catégories
  async getAllCategories() {
    try {
      const response = await api.get('/products/categories');
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Vérifier la disponibilité d'un produit
  async checkProductAvailability(productId, quantity) {
    try {
      const response = await api.get(`/products/${productId}/availability`, {
        params: { quantity }
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Récupérer les produits avec stock faible (pour l'administration)
  async getProductsWithLowStock(threshold = 10) {
    try {
      const response = await api.get('/products/low-stock', {
        params: { threshold }
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Récupérer les produits en rupture de stock (pour l'administration)
  async getOutOfStockProducts() {
    try {
      const response = await api.get('/products/out-of-stock');
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Test de l'API des produits
  async testProductsAPI() {
    try {
      const response = await api.get('/products/test');
      return response.data;
    } catch (error) {
      throw error;
    }
  }
}

export default new ProductService();
