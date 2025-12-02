import React from 'react';

export default function ProductCard({ produit, onAddToCart }) {
  const handleAddToCart = () => {
    if (onAddToCart) {
      onAddToCart(produit);
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow duration-300 overflow-hidden">
      <div className="relative">
        <img
          src={produit.imageUrl || 'https://via.placeholder.com/300x200?text=Produit'}
          alt={produit.name}
          className="w-full h-48 object-cover"
          onError={(e) => {
            e.target.src = 'https://via.placeholder.com/300x200?text=Produit';
          }}
        />
        {produit.stockQuantity <= 5 && produit.stockQuantity > 0 && (
          <div className="absolute top-2 right-2 bg-orange-500 text-white px-2 py-1 rounded-full text-xs">
            Stock faible
          </div>
        )}
        {produit.stockQuantity === 0 && (
          <div className="absolute top-2 right-2 bg-red-500 text-white px-2 py-1 rounded-full text-xs">
            Rupture
          </div>
        )}
      </div>
      
      <div className="p-4">
        <div className="mb-2">
          <span className="text-xs text-gray-500 bg-gray-100 px-2 py-1 rounded-full">
            {produit.category}
          </span>
        </div>
        
        <h3 className="text-lg font-semibold text-gray-800 mb-2 line-clamp-2">
          {produit.name}
        </h3>
        
        {produit.description && (
          <p className="text-sm text-gray-600 mb-3 line-clamp-2">
            {produit.description}
          </p>
        )}
        
        <div className="flex items-center justify-between mb-4">
          <span className="text-xl font-bold text-sky-600">
            {produit.price} MAD
          </span>
          <span className="text-sm text-gray-500">
            Stock: {produit.stockQuantity}
          </span>
        </div>
        
        <button
          onClick={handleAddToCart}
          disabled={produit.stockQuantity === 0 || !produit.available}
          className={`w-full py-2 px-4 rounded-lg font-medium transition-colors ${
            produit.stockQuantity === 0 || !produit.available
              ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
              : 'bg-sky-600 hover:bg-sky-700 text-white'
          }`}
        >
          {produit.stockQuantity === 0 
            ? 'Rupture de stock' 
            : !produit.available 
            ? 'Indisponible'
            : '🛒 Ajouter au panier'
          }
        </button>
      </div>
    </div>
  );
}