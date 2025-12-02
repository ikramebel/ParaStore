import { createContext, useState, useContext, useEffect } from 'react';
import cartService from '../services/cartService';
import toast from 'react-hot-toast';
import { useAuth } from './AuthContext';

const CartContext = createContext();

export function CartProvider({ children }) {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(false);
  const [itemCount, setItemCount] = useState(0);
  const { isAuthenticated } = useAuth();

  // Charger le panier au démarrage si l'utilisateur est connecté
  useEffect(() => {
    if (isAuthenticated) {
      loadCart();
    } else {
      // Réinitialiser le panier si l'utilisateur n'est pas connecté
      setCart(null);
      setItemCount(0);
    }
  }, [isAuthenticated]);

  // Fonction pour charger le panier depuis l'API
  const loadCart = async () => {
    try {
      setLoading(true);
      const cartData = await cartService.getCart();
      setCart(cartData);
      
      // Calculer le nombre total d'articles
      const totalItems = cartData.items?.reduce((total, item) => total + item.quantity, 0) || 0;
      setItemCount(totalItems);
    } catch (error) {
      console.error('Erreur lors du chargement du panier:', error);
      // En cas d'erreur, initialiser un panier vide
      setCart({ items: [], totalAmount: 0 });
      setItemCount(0);
    } finally {
      setLoading(false);
    }
  };

  // Fonction pour ajouter un article au panier
  const addToCart = async (productId, quantity = 1) => {
    try {
      setLoading(true);
      const updatedCart = await cartService.addToCart(productId, quantity);
      setCart(updatedCart);
      
      // Mettre à jour le compteur d'articles
      const totalItems = updatedCart.items?.reduce((total, item) => total + item.quantity, 0) || 0;
      setItemCount(totalItems);
      
      toast.success('Produit ajouté au panier !');
      return updatedCart;
    } catch (error) {
      const errorMessage = error.response?.data?.error || 'Erreur lors de l\'ajout au panier';
      toast.error(errorMessage);
      throw error;
    } finally {
      setLoading(false);
    }
  };

  // Fonction pour mettre à jour la quantité d'un article
  const updateCartItem = async (itemId, quantity) => {
    try {
      setLoading(true);
      const updatedCart = await cartService.updateCartItem(itemId, quantity);
      setCart(updatedCart);
      
      // Mettre à jour le compteur d'articles
      const totalItems = updatedCart.items?.reduce((total, item) => total + item.quantity, 0) || 0;
      setItemCount(totalItems);
      
      toast.success('Quantité mise à jour !');
      return updatedCart;
    } catch (error) {
      const errorMessage = error.response?.data?.error || 'Erreur lors de la mise à jour';
      toast.error(errorMessage);
      throw error;
    } finally {
      setLoading(false);
    }
  };

  // Fonction pour supprimer un article du panier
  const removeFromCart = async (itemId) => {
    try {
      setLoading(true);
      const updatedCart = await cartService.removeFromCart(itemId);
      setCart(updatedCart);
      
      // Mettre à jour le compteur d'articles
      const totalItems = updatedCart.items?.reduce((total, item) => total + item.quantity, 0) || 0;
      setItemCount(totalItems);
      
      toast.success('Produit supprimé du panier !');
      return updatedCart;
    } catch (error) {
      const errorMessage = error.response?.data?.error || 'Erreur lors de la suppression';
      toast.error(errorMessage);
      throw error;
    } finally {
      setLoading(false);
    }
  };

  // Fonction pour vider le panier
  const clearCart = async () => {
    try {
      setLoading(true);
      await cartService.clearCart();
      setCart({ items: [], totalAmount: 0 });
      setItemCount(0);
      toast.success('Panier vidé !');
    } catch (error) {
      const errorMessage = error.response?.data?.error || 'Erreur lors du vidage du panier';
      toast.error(errorMessage);
      throw error;
    } finally {
      setLoading(false);
    }
  };

  // Fonction pour valider le panier
  const validateCart = async () => {
    try {
      const validation = await cartService.validateCart();
      if (!validation.valid) {
        toast.warning(validation.message);
        // Recharger le panier pour avoir les données à jour
        await loadCart();
      }
      return validation;
    } catch (error) {
      const errorMessage = error.response?.data?.error || 'Erreur lors de la validation du panier';
      toast.error(errorMessage);
      throw error;
    }
  };

  const value = {
    cart,
    loading,
    itemCount,
    loadCart,
    addToCart,
    updateCartItem,
    removeFromCart,
    clearCart,
    validateCart,
  };

  return (
    <CartContext.Provider value={value}>
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
}
