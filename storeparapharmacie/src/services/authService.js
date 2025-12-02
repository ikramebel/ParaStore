import api from '../config/api';

class AuthService {
  // Inscription d'un nouvel utilisateur
  async register(userData) {
    try {
      const response = await api.post('/auth/register', userData);
      const { token, user } = response.data;
      
      // Stocker le token et les informations utilisateur
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(user));
      
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Connexion d'un utilisateur
  async login(credentials) {
    try {
      const response = await api.post('/auth/login', credentials);
      const { token, user } = response.data;
      
      // Stocker le token et les informations utilisateur
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(user));
      
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Déconnexion
  async logout() {
    try {
      await api.post('/auth/logout');
    } catch (error) {
      console.error('Erreur lors de la déconnexion:', error);
    } finally {
      // Nettoyer le stockage local même en cas d'erreur
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    }
  }

  // Récupérer les informations de l'utilisateur connecté
  async getCurrentUser() {
    try {
      const response = await api.get('/auth/me');
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Valider un token
  async validateToken(token) {
    try {
      const response = await api.post('/auth/validate', { token });
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  // Vérifier si l'utilisateur est connecté
  isAuthenticated() {
    const token = localStorage.getItem('token');
    return !!token;
  }

  // Récupérer le token stocké
  getToken() {
    return localStorage.getItem('token');
  }

  // Récupérer les informations utilisateur stockées
  getUser() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }

  // Vérifier si le token est expiré (optionnel - nécessite de décoder le JWT)
  isTokenExpired() {
    const token = this.getToken();
    if (!token) return true;

    try {
      // Décoder le payload du JWT (partie centrale)
      const payload = JSON.parse(atob(token.split('.')[1]));
      const currentTime = Date.now() / 1000;
      
      return payload.exp < currentTime;
    } catch (error) {
      return true;
    }
  }

  // Auto-login au démarrage de l'application
  async autoLogin() {
    const token = this.getToken();
    if (!token || this.isTokenExpired()) {
      this.logout();
      return null;
    }

    try {
      // Valider le token avec le serveur
      const validation = await this.validateToken(token);
      if (validation.valid) {
        return this.getUser();
      } else {
        this.logout();
        return null;
      }
    } catch (error) {
      this.logout();
      return null;
    }
  }
}

export default new AuthService();
