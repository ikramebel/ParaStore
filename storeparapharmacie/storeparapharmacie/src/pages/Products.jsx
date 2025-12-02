import React, { useState } from 'react';
import ProductCard from '../components/ProductCard';

export default function Products() {
  const categories = [
    'Soins du visage et de la peau',
    'Compléments alimentaires et bien-être',
    'Hygiène & soins corporels',
  ];

  const [activeCategory, setActiveCategory] = useState('Toutes');
  const [allProducts, setAllProducts] = useState([]);

  useEffect(() => {
    fetch('http://localhost:8080/api/products') // adapte l'URL selon ton backend
      .then(res => res.json())
      .then(data => setAllProducts(data))
      .catch(err => console.error(err));
  }, []);
  



  const filteredProducts =
    activeCategory === 'Toutes'
      ? allProducts
      : allProducts.filter(p => p.category === activeCategory);

  return (
    <div className="p-6">
      <h1 className="text-3xl font-extrabold text-center text-gray-900 mb-6">Nos Produits</h1>

      {/* FILTRES */}
      <div className="flex flex-wrap justify-center gap-4 mb-10">
        <button
          onClick={() => setActiveCategory('Toutes')}
          className={`px-4 py-2 rounded-full text-sm font-medium border ${
            activeCategory === 'Toutes'
              ? 'bg-teal-500 text-white'
              : 'bg-white text-teal-500 border-teal-500'
          }`}
        >
          Toutes
        </button>
        {categories.map((cat, index) => (
          <button
            key={index}
            onClick={() => setActiveCategory(cat)}
            className={`px-4 py-2 rounded-full text-sm font-medium border ${
              activeCategory === cat
                ? 'bg-teal-500 text-white'
                : 'bg-white text-teal-500 border-teal-500'
            }`}
          >
            {cat}
          </button>
        ))}
      </div>

      {/* AFFICHAGE DES PRODUITS */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredProducts.map(product => (
          <ProductCard key={product.id} produit={product} />
        ))}
      </div>
    </div>
  );
}
