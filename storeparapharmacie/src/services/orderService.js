import api from '../config/api';

class OrderService {
  // Créer une nouvelle commande
  async createOrder(orderData) {
    try {
      const response = await api.post('/orders', orderData);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Récupérer toutes les commandes de l'utilisateur
  async getUserOrders() {
    try {
      const response = await api.get('/orders');
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Récupérer les commandes avec pagination
  async getUserOrdersWithPagination(page = 0, size = 10) {
    try {
      const response = await api.get('/orders/paginated', {
        params: { page, size }
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Récupérer une commande spécifique par son ID
  async getOrder(orderId) {
    try {
      const response = await api.get(`/orders/${orderId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Annuler une commande
  async cancelOrder(orderId) {
    try {
      const response = await api.put(`/orders/${orderId}/cancel`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Récupérer les statistiques des commandes de l'utilisateur
  async getUserOrderStats() {
    try {
      const response = await api.get('/orders/stats');
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Méthodes d'administration (nécessitent le rôle ADMIN)

  // Récupérer toutes les commandes (administration)
  async getAllOrders() {
    try {
      const response = await api.get('/orders/admin/all');
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Mettre à jour le statut d'une commande (administration)
  async updateOrderStatus(orderId, status) {
    try {
      const response = await api.put(`/orders/admin/${orderId}/status`, {
        status
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Récupérer les commandes par statut (administration)
  async getOrdersByStatus(status) {
    try {
      const response = await api.get(`/orders/admin/status/${status}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Récupérer les commandes récentes (administration)
  async getRecentOrders() {
    try {
      const response = await api.get('/orders/admin/recent');
      return response.data;
    } catch (error) {
      throw error;
    }
  }
}

export default new OrderService();
