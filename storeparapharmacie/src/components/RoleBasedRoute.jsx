import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const RoleBasedRoute = ({ children, allowedRoles }) => {
  const { user, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-sky-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Chargement...</p>
        </div>
      </div>
    );
  }

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(user.role)) {
    // Rediriger vers la page appropriée selon le rôle de l'utilisateur
    const redirectPath = getRedirectPathForRole(user.role);
    return <Navigate to={redirectPath} replace />;
  }

  return children;
};

// Fonction helper pour obtenir le chemin de redirection selon le rôle
const getRedirectPathForRole = (role) => {
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

export default RoleBasedRoute;
