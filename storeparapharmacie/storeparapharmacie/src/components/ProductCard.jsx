import React from 'react';

export default function ProductCard({ produit, onAddToCart }) {
  return (
    <div className="bg-white rounded-lg shadow p-4 hover:shadow-lg transition">
      <img
        src={produit.image || 'https://via.placeholder.com/150'}
        alt={produit.nom}
        className="w-full h-40 object-cover rounded mb-4"
      />
      <h3 className="text-lg font-semibold text-gray-800 mb-2">{produit.nom}</h3>
      <p className="text-teal-600 font-bold mb-2">{produit.prix} MAD</p>
      <button
        onClick={() => onAddToCart(produit)}
        className="bg-teal-500 hover:bg-teal-600 text-white px-4 py-2 rounded w-full"
      >
        🛒 Ajouter au panier
      </button>
    </div>
  );
}

