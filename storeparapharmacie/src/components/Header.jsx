import React from 'react';
import { FaUser, FaShoppingCart } from "react-icons/fa";
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';

function Header() {
  const { user, isAuthenticated, logout } = useAuth();
  const { itemCount } = useCart();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logout();
      navigate('/');
    } catch (error) {
      console.error('Erreur lors de la déconnexion:', error);
    }
  };

  const handleUserIconClick = () => {
    if (isAuthenticated) {
      // Si connecté, on peut rediriger vers un profil ou afficher un menu
      // Pour l'instant, on ne fait rien
    } else {
      navigate('/login');
    }
  };

  return (
    <header className="flex items-center justify-between px-6 py-4 shadow bg-gradient-to-r from-emerald-50 to-blue-50">
      {/* Logo */}
      <Link to="/" className="flex items-center gap-2">
        <img src="ParaStore (1).png" alt="Logo Parastore" className="h-14 w-16 object-contain" />
        <h1 className="text-2xl font-bold text-emerald-600">Parastore</h1>
      </Link>

      {/* Navigation */}
      <nav className="flex gap-6 text-gray-700 font-medium text-lg lg:text-xl">
        {[
          { to: '/', label: 'Home' },
          { to: '/products', label: 'Products' },
          { to: '/about', label: 'About Us' },
          { to: '/contact', label: 'Contact' }
        ].map(link => (
          <NavLink
            key={link.to}
            to={link.to}
            className={({ isActive }) =>
              `transition-all cursor-pointer px-2 py-1 rounded 
              ${isActive ? 'text-emerald-700 underline font-bold scale-110' : ''}
              hover:scale-110  hover:text-emerald-600`
            }
          >
            {link.label}
          </NavLink>
        ))}
      </nav>

      {/* Icons et actions utilisateur */}
      <div className="flex items-center gap-4 text-sky-600 text-lg">
      
        
        {/* Icône utilisateur avec gestion de l'authentification */}
        <div className="relative">
        <FaUser 
          className="cursor-pointer text-emerald-600 transition-all hover:scale-110 hover:shadow-lg hover:text-emerald-700" 
          onClick={handleUserIconClick}
        />
        
        {/* Menu déroulant pour utilisateur connecté */}
        {isAuthenticated && (
          <div className="absolute right-0 top-8 min-w-[150px] z-10 hidden group-hover:block">
            <p className="text-sm text-gray-700 px-2 py-1">
              {user?.firstName || user?.name || 'Utilisateur'}
            </p>
            <hr className="my-1" />
            <button
              onClick={handleLogout}
              className="text-sm text-red-600 hover:text-red-800 px-2 py-1 w-full text-left"
            >
              Déconnexion
            </button>
          </div>
        )}
        </div>

        {/* Panier avec compteur */}
      <Link 
        to="/cart" 
        className="relative text-xl text-emerald-600 transition-all hover:scale-110 hover:shadow-lg hover:text-emerald-700"
      >
        <FaShoppingCart className="cursor-pointer" />
          {isAuthenticated && itemCount > 0 && (
            <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
              {itemCount > 99 ? '99+' : itemCount}
            </span>
          )}
        </Link>

    

        {/* Informations utilisateur connecté */}
        {isAuthenticated && (
          <div className="flex items-center gap-2 ml-2">
            <span className="text-sm text-gray-700">
              {user?.firstName || user?.name || 'Utilisateur'}
            </span>
            <button
              onClick={handleLogout}
              className="text-sm bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded transition"
            >
              Déconnexion
            </button>
          </div>
        )}
      </div>
    </header>
  );
}

export default Header;