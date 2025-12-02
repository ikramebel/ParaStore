import React, { useState, useEffect } from 'react';
import ProductCard from '../components/ProductCard';
import { useAuth } from '../context/AuthContext';

export default function Products() {
  const { user } = useAuth();
  const [allProducts, setAllProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [activeCategory, setActiveCategory] = useState('Toutes');
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [cart, setCart] = useState([]);

  // Fonction pour récupérer le token
  const getAuthHeaders = () => {
    const token = localStorage.getItem('token');
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    };
  };

  // Charger les produits
  const loadProducts = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/products', {
        headers: getAuthHeaders()
      });
      if (response.ok) {
        const data = await response.json();
        setAllProducts(data);
      } else {
        console.error('Erreur lors du chargement des produits');
      }
    } catch (error) {
      console.error('Erreur lors du chargement des produits:', error);
    } finally {
      setLoading(false);
    }
  };

  // Charger les catégories
  const loadCategories = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/products/categories', {
        headers: getAuthHeaders()
      });
      if (response.ok) {
        const data = await response.json();
        setCategories(data);
      }
    } catch (error) {
      console.error('Erreur lors du chargement des catégories:', error);
    }
  };

  // Charger le panier
  const loadCart = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/cart', {
        headers: getAuthHeaders()
      });
      if (response.ok) {
        const data = await response.json();
        setCart(data.items || []);
      }
    } catch (error) {
      console.error('Erreur lors du chargement du panier:', error);
    }
  };

  // Ajouter au panier
  const addToCart = async (product) => {
    try {
      const response = await fetch('http://localhost:8080/api/cart/add', {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify({
          productId: product.id,
          quantity: 1
        })
      });

      if (response.ok) {
        // Mettre à jour le panier local
        const existingItem = cart.find(item => item.product.id === product.id);
        if (existingItem) {
          setCart(cart.map(item => 
            item.product.id === product.id 
              ? { ...item, quantity: item.quantity + 1 }
              : item
          ));
        } else {
          setCart([...cart, { product, quantity: 1 }]);
        }
        
        // Afficher un message de succès
        alert('Produit ajouté au panier !');
      } else {
        alert('Erreur lors de l\'ajout au panier');
      }
    } catch (error) {
      console.error('Erreur lors de l\'ajout au panier:', error);
      alert('Erreur lors de l\'ajout au panier');
    }
  };

  useEffect(() => {
    loadProducts();
    loadCategories();
    loadCart();
  }, []);

  // Filtrer les produits
  const filteredProducts = allProducts.filter(product => {
    const matchesCategory = activeCategory === 'Toutes' || product.category === activeCategory;
    const matchesSearch = product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         product.description.toLowerCase().includes(searchTerm.toLowerCase());
    return matchesCategory && matchesSearch && product.available;
  });

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-emerald-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Chargement des produits...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <div className="flex flex-col items-center">
            <h1 className="text-3xl font-bold text-emerald-700 text-center">Nos Produits</h1>
            <p className="mt-2 text-gray-700 font-semibold text-center">
              Découvrez notre sélection de produits de parapharmacie
            </p>
            <div className="flex items-center space-x-4 mt-4">
              <span className="text-sm text-gray-600">
                Bienvenue, {user?.name || 'Invité'}!
              </span>
              <div className="relative">
                <span className="bg-emerald-600 text-white px-3 py-1 rounded-full text-sm">
                  Panier ({cart.reduce((total, item) => total + item.quantity, 0)})
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Barre de recherche centrée */}
        <div className="mb-8 flex justify-center">
          <input
            type="text"
            placeholder="Rechercher un produit..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full max-w-md px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-emerald-500"
          />
        </div>

        {/* Filtres par catégorie */}
        <div className="flex flex-wrap gap-4 mb-8 justify-center">
          <button
            onClick={() => setActiveCategory('Toutes')}
            className={`px-4 py-2 rounded-full text-sm font-medium border transition ${
              activeCategory === 'Toutes'
                ? 'bg-emerald-600 text-white border-emerald-600'
                : 'bg-white text-emerald-600 border-emerald-600 hover:bg-emerald-50'
            }`}
          >
            Toutes ({allProducts.length})
          </button>
          {categories.map((category) => {
            const count = allProducts.filter(p => p.category === category).length;
            return (
              <button
                key={category}
                onClick={() => setActiveCategory(category)}
                className={`px-4 py-2 rounded-full text-sm font-medium border transition ${
                  activeCategory === category
                    ? 'bg-emerald-600 text-white border-emerald-600'
                    : 'bg-white text-emerald-600 border-emerald-600 hover:bg-emerald-50'
                }`}
              >
                {category} ({count})
              </button>
            );
          })}
        </div>

        {/* Affichage des produits */}
        {filteredProducts.length > 0 ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {filteredProducts.map(product => (
              <div
                key={product.id}
                className="bg-white p-4 rounded-lg shadow flex flex-col h-full"
              >
                <div className="w-full h-48 flex items-center justify-center mb-4 bg-gray-50 rounded">
                  <img
                    src={product.imageUrl}
                    alt={product.name}
                    className="object-contain h-full max-h-44 w-full"
                  />
                </div>
                <h3 className="text-lg font-bold text-emerald-700 mb-1">{product.name}</h3>
                <p className="text-gray-700 mb-2">{product.description}</p>
                <div className="text-emerald-700 font-bold text-lg mb-2">{product.price} MAD</div>
                <div className="mt-auto">
                  <button
                    onClick={() => addToCart(product)}
                    className="w-full bg-emerald-600 hover:bg-emerald-700 text-white py-2 rounded-xl font-semibold transition-all shadow hover:shadow-2xl"
                  >
                    Ajouter au panier
                  </button>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="text-center py-12">
            <p className="text-gray-500 text-lg">
              {searchTerm ? 
                `Aucun produit trouvé pour "${searchTerm}"` : 
                'Aucun produit disponible dans cette catégorie'
              }
            </p>
          </div>
        )}
      </div>
    </div>
  );
}