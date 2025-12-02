import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';

export default function Cart() {
  const { user } = useAuth();
  const [cart, setCart] = useState([]);
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);

  // Fonction pour récupérer le token
  const getAuthHeaders = () => {
    const token = localStorage.getItem('token');
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    };
  };

  // Charger le panier
  const loadCart = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/cart', {
        headers: getAuthHeaders()
      });
      if (response.ok) {
        const data = await response.json();
        setCart(data.items || []);
      } else {
        console.error('Erreur lors du chargement du panier');
      }
    } catch (error) {
      console.error('Erreur lors du chargement du panier:', error);
    } finally {
      setLoading(false);
    }
  };

  // Modifier la quantité d'un produit
  const updateQuantity = async (cartItemId, newQuantity) => {
    if (newQuantity < 1) return;
    
    setUpdating(true);
    try {
      const response = await fetch(`http://localhost:8080/api/cart/update/${cartItemId}`, {
        method: 'PUT',
        headers: getAuthHeaders(),
        body: JSON.stringify({ quantity: newQuantity })
      });

      if (response.ok) {
        setCart(cart.map(item => 
          item.id === cartItemId 
            ? { ...item, quantity: newQuantity }
            : item
        ));
      } else {
        alert('Erreur lors de la modification de la quantité');
      }
    } catch (error) {
      console.error('Erreur lors de la modification:', error);
      alert('Erreur lors de la modification de la quantité');
    } finally {
      setUpdating(false);
    }
  };

  // Supprimer un produit du panier
  const removeFromCart = async (cartItemId) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer ce produit du panier ?')) {
      setUpdating(true);
      try {
        const response = await fetch(`http://localhost:8080/api/cart/remove/${cartItemId}`, {
          method: 'DELETE',
          headers: getAuthHeaders()
        });

        if (response.ok) {
          setCart(cart.filter(item => item.id !== cartItemId));
        } else {
          alert('Erreur lors de la suppression');
        }
      } catch (error) {
        console.error('Erreur lors de la suppression:', error);
        alert('Erreur lors de la suppression');
      } finally {
        setUpdating(false);
      }
    }
  };

  // Passer la commande
  const placeOrder = async () => {
    if (cart.length === 0) {
      alert('Votre panier est vide');
      return;
    }

    setUpdating(true);
    try {
      const response = await fetch('http://localhost:8080/api/orders', {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify({
          items: cart.map(item => ({
            productId: item.product.id,
            quantity: item.quantity
          }))
        })
      });

      if (response.ok) {
        alert('Commande passée avec succès !');
        setCart([]); // Vider le panier
      } else {
        const errorData = await response.json();
        alert(errorData.error || 'Erreur lors de la commande');
      }
    } catch (error) {
      console.error('Erreur lors de la commande:', error);
      alert('Erreur lors de la commande');
    } finally {
      setUpdating(false);
    }
  };

  useEffect(() => {
    loadCart();
  }, []);

  // Calculer le total
  const total = cart.reduce((acc, item) => acc + (item.product.price * item.quantity), 0);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-sky-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Chargement du panier...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <div className="flex justify-between items-center">
            <div>
              <h1 className="text-3xl font-bold text-gray-900">🛒 Votre Panier</h1>
              <p className="mt-2 text-gray-600">
                {cart.length} article{cart.length > 1 ? 's' : ''} dans votre panier
              </p>
            </div>
            <Link 
              to="/products"
              className="bg-sky-600 text-white px-4 py-2 rounded-lg hover:bg-sky-700 transition"
            >
              Continuer les achats
            </Link>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {cart.length === 0 ? (
          <div className="text-center py-12">
            <div className="text-6xl mb-4">🛒</div>
            <h2 className="text-2xl font-semibold text-gray-800 mb-4">Votre panier est vide</h2>
            <p className="text-gray-600 mb-8">Découvrez nos produits et ajoutez-les à votre panier</p>
            <Link 
              to="/products"
              className="bg-sky-600 text-white px-6 py-3 rounded-lg hover:bg-sky-700 transition"
            >
              Voir nos produits
            </Link>
          </div>
        ) : (
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* Liste des produits */}
            <div className="lg:col-span-2 space-y-4">
              {cart.map((item) => (
                <div
                  key={item.id}
                  className="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition-shadow"
                >
                  <div className="flex items-center space-x-4">
                    <img 
                      src={item.product.imageUrl || 'https://via.placeholder.com/100x100?text=Produit'} 
                      alt={item.product.name}
                      className="w-20 h-20 object-cover rounded-lg"
                      onError={(e) => {
                        e.target.src = 'https://via.placeholder.com/100x100?text=Produit';
                      }}
                    />
                    
                    <div className="flex-1">
                      <h3 className="text-lg font-semibold text-gray-800">
                        {item.product.name}
                      </h3>
                      <p className="text-sm text-gray-500 mb-2">
                        {item.product.category}
                      </p>
                      <p className="text-lg font-bold text-sky-600">
                        {item.product.price} € × {item.quantity}
                      </p>
                    </div>

                    <div className="flex items-center space-x-3">
                      <div className="flex items-center space-x-2">
                        <button
                          onClick={() => updateQuantity(item.id, item.quantity - 1)}
                          disabled={updating || item.quantity <= 1}
                          className="w-8 h-8 rounded-full bg-gray-200 hover:bg-gray-300 flex items-center justify-center disabled:opacity-50"
                        >
                          -
                        </button>
                        <span className="w-12 text-center font-medium">
                          {item.quantity}
                        </span>
                        <button
                          onClick={() => updateQuantity(item.id, item.quantity + 1)}
                          disabled={updating || item.quantity >= item.product.stockQuantity}
                          className="w-8 h-8 rounded-full bg-gray-200 hover:bg-gray-300 flex items-center justify-center disabled:opacity-50"
                        >
                          +
                        </button>
                      </div>

                      <button
                        onClick={() => removeFromCart(item.id)}
                        disabled={updating}
                        className="text-red-600 hover:text-red-800 p-2 disabled:opacity-50"
                      >
                        🗑️
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            {/* Résumé de la commande */}
            <div className="lg:col-span-1">
              <div className="bg-white p-6 rounded-lg shadow-md sticky top-4">
                <h3 className="text-xl font-semibold text-gray-800 mb-4">
                  Résumé de la commande
                </h3>
                
                <div className="space-y-3 mb-6">
                  <div className="flex justify-between">
                    <span className="text-gray-600">Sous-total</span>
                    <span className="font-medium">{total.toFixed(2)} €</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Livraison</span>
                    <span className="font-medium">Gratuite</span>
                  </div>
                  <hr />
                  <div className="flex justify-between text-lg font-bold">
                    <span>Total</span>
                    <span className="text-sky-600">{total.toFixed(2)} €</span>
                  </div>
                </div>

                <button
                  onClick={placeOrder}
                  disabled={updating || cart.length === 0}
                  className="w-full bg-sky-600 text-white py-3 rounded-lg hover:bg-sky-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  {updating ? 'Traitement...' : 'Passer la commande'}
                </button>

                <p className="text-xs text-gray-500 mt-4 text-center">
                  Paiement sécurisé et livraison gratuite
                </p>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}