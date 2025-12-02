import axios from 'axios';
import toast from 'react-hot-toast';

// Configuration de base de l'API
const API_BASE_URL = 'http://localhost:8080/api';

// Création de l'instance axios
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Intercepteur pour ajouter le token JWT aux requêtes
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Intercepteur pour gérer les réponses et les erreurs
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    const { response } = error;
    
    if (response) {
      switch (response.status) {
        case 401:
          // Token expiré ou invalide
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          toast.error('Session expirée. Veuillez vous reconnecter.');
          window.location.href = '/login';
          break;
        case 403:
          toast.error('Accès refusé');
          break;
        case 404:
          toast.error('Ressource non trouvée');
          break;
        case 500:
          toast.error('Erreur serveur. Veuillez réessayer plus tard.');
          break;
        default:
          if (response.data?.error) {
            toast.error(response.data.error);
          } else {
            toast.error('Une erreur est survenue');
          }
      }
    } else {
      // Erreur réseau
      toast.error('Erreur de connexion. Vérifiez votre connexion internet.');
    }
    
    return Promise.reject(error);
  }
);

export default api;
export { API_BASE_URL };