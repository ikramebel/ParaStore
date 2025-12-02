import React, { useState } from 'react';

const mockCart = [
  {
    id: 1,
    nom: 'Crème hydratante Nivea',
    prix: 45,
    quantite: 2,
    image: 'https://via.placeholder.com/100'
  },
  {
    id: 2,
    nom: 'Oméga-3 Arkopharma',
    prix: 85,
    quantite: 1,
    image: 'https://via.placeholder.com/100'
  }
];

export default function Cart() {
  const [panier, setPanier] = useState(mockCart);

  const modifierQuantite = (id, nouvelleQuantite) => {
    setPanier((prev) =>
      prev.map((item) =>
        item.id === id ? { ...item, quantite: nouvelleQuantite } : item
      )
    );
  };

  const supprimerProduit = (id) => {
    setPanier((prev) => prev.filter((item) => item.id !== id));
  };

  const total = panier.reduce((acc, item) => acc + item.prix * item.quantite, 0);

  return (
    <div className="min-h-screen bg-gray-50 px-6 py-12">
      <div className="max-w-5xl mx-auto">
        <h1 className="text-3xl font-bold mb-8 text-teal-600">🛒 Votre Panier</h1>

        {panier.length === 0 ? (
          <p className="text-center text-gray-600">Votre panier est vide.</p>
        ) : (
          <div className="space-y-6">
            {panier.map((produit) => (
              <div
                key={produit.id}
                className="flex items-center bg-white p-4 rounded shadow-sm hover:shadow-md transition"
              >
                <img src={produit.image} alt={produit.nom} className="w-24 h-24 object-cover rounded" />
                <div className="ml-4 flex-1">
                  <h2 className="text-lg font-semibold">{produit.nom}</h2>
                  <p className="text-gray-600">{produit.prix} MAD</p>
                  <div className="flex items-center mt-2 space-x-3">
                    <label>Quantité :</label>
                    <input
                      type="number"
                      min="1"
                      value={produit.quantite}
                      onChange={(e) => modifierQuantite(produit.id, parseInt(e.target.value))}
                      className="w-16 border rounded px-2 py-1"
                    />
                  </div>
                </div>
                <button
                  onClick={() => supprimerProduit(produit.id)}
                  className="ml-4 text-red-600 hover:text-red-800"
                >
                  Supprimer
                </button>
              </div>
            ))}

            <div className="text-right mt-6">
              <p className="text-xl font-semibold">
                Total : <span className="text-teal-600">{total} MAD</span>
              </p>
              <button className="mt-4 bg-teal-500 hover:bg-teal-600 text-white px-6 py-2 rounded">
                Passer la commande
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
