import { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null); // null = non connecté
  const [loading, setLoading] = useState(true);

  // Vérifier si un token existe au chargement de l'app
  useEffect(() => {
    const token = localStorage.getItem('token');
    const userData = localStorage.getItem('user');
    
    if (token && userData) {
      try {
        const parsedUser = JSON.parse(userData);
        setUser(parsedUser);
      } catch (error) {
        console.error('Erreur lors du parsing des données utilisateur:', error);
        localStorage.removeItem('token');
        localStorage.removeItem('user');
      }
    }
    setLoading(false);
  }, []);

  const login = (userData, token) => {
    // Stocker les données utilisateur et le token
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(userData));
    setUser(userData);
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  };

  // Fonction pour obtenir la page de redirection selon le rôle
  const getRedirectPath = (role) => {
    switch (role) {
      case 'ADMIN':
        return '/admin';
      case 'MANAGER':
        return '/manager';
      case 'USER':
      default:
        return '/products';
    }
  };

  // Fonction pour vérifier si l'utilisateur a un rôle spécifique
  const hasRole = (role) => {
    return user && user.role === role;
  };

  // Fonction pour vérifier si l'utilisateur a l'un des rôles spécifiés
  const hasAnyRole = (roles) => {
    return user && roles.includes(user.role);
  };

  const value = {
    user,
    login,
    logout,
    loading,
    isAuthenticated: !!user,
    getRedirectPath,
    hasRole,
    hasAnyRole
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}