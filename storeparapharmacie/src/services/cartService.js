import api from '../config/api';

class CartService {
  // Récupérer le contenu du panier
  async getCart() {
    try {
      const response = await api.get('/cart');
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Ajouter un article au panier
  async addToCart(productId, quantity = 1) {
    try {
      const response = await api.post('/cart/items', {
        productId,
        quantity
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Mettre à jour la quantité d'un article dans le panier
  async updateCartItem(itemId, quantity) {
    try {
      const response = await api.put(`/cart/items/${itemId}`, {
        quantity
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Supprimer un article du panier
  async removeFromCart(itemId) {
    try {
      const response = await api.delete(`/cart/items/${itemId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Vider complètement le panier
  async clearCart() {
    try {
      const response = await api.delete('/cart');
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Récupérer le nombre d'articles dans le panier
  async getCartItemCount() {
    try {
      const response = await api.get('/cart/count');
      return response.data.count;
    } catch (error) {
      throw error;
    }
  }

  // Récupérer la quantité totale d'articles dans le panier
  async getTotalQuantity() {
    try {
      const response = await api.get('/cart/quantity');
      return response.data.totalQuantity;
    } catch (error) {
      throw error;
    }
  }

  // Valider le panier (vérifier la disponibilité des articles)
  async validateCart() {
    try {
      const response = await api.get('/cart/validate');
      return response.data;
    } catch (error) {
      throw error;
    }
  }
}

export default new CartService();
