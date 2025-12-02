import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import ProductForm from '../components/ProductForm';

const ManagerPage = () => {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('products');
  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showProductForm, setShowProductForm] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);

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
        setProducts(data);
      }
    } catch (error) {
      console.error('Erreur lors du chargement des produits:', error);
    } finally {
      setLoading(false);
    }
  };

  // Charger les commandes
  const loadOrders = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/manager/orders', {
        headers: getAuthHeaders()
      });
      if (response.ok) {
        const data = await response.json();
        setOrders(data);
      }
    } catch (error) {
      console.error('Erreur lors du chargement des commandes:', error);
    } finally {
      setLoading(false);
    }
  };

  // Ajouter un produit
  const handleAddProduct = async (productData) => {
    try {
      const response = await fetch('http://localhost:8080/api/manager/products', {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify(productData)
      });

      if (response.ok) {
        setShowProductForm(false);
        loadProducts(); // Recharger la liste
      } else {
        throw new Error('Erreur lors de l\'ajout du produit');
      }
    } catch (error) {
      throw error;
    }
  };

  // Modifier un produit
  const handleEditProduct = async (productData) => {
    try {
      const response = await fetch(`http://localhost:8080/api/manager/products/${editingProduct.id}`, {
        method: 'PUT',
        headers: getAuthHeaders(),
        body: JSON.stringify(productData)
      });

      if (response.ok) {
        setShowProductForm(false);
        setEditingProduct(null);
        loadProducts(); // Recharger la liste
      } else {
        throw new Error('Erreur lors de la modification du produit');
      }
    } catch (error) {
      throw error;
    }
  };

  // Supprimer un produit
  const handleDeleteProduct = async (productId) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer ce produit ?')) {
      try {
        const response = await fetch(`http://localhost:8080/api/manager/products/${productId}`, {
          method: 'DELETE',
          headers: getAuthHeaders()
        });

        if (response.ok) {
          loadProducts(); // Recharger la liste
        } else {
          alert('Erreur lors de la suppression du produit');
        }
      } catch (error) {
        console.error('Erreur lors de la suppression:', error);
        alert('Erreur lors de la suppression du produit');
      }
    }
  };

  // Modifier le statut d'une commande
  const handleUpdateOrderStatus = async (orderId, newStatus) => {
    try {
      const response = await fetch(`http://localhost:8080/api/manager/orders/${orderId}/status?status=${newStatus}`, {
        method: 'PUT',
        headers: getAuthHeaders()
      });

      if (response.ok) {
        loadOrders(); // Recharger la liste
      } else {
        alert('Erreur lors de la modification du statut');
      }
    } catch (error) {
      console.error('Erreur lors de la modification du statut:', error);
      alert('Erreur lors de la modification du statut');
    }
  };

  useEffect(() => {
    if (activeTab === 'products') {
      loadProducts();
    } else if (activeTab === 'orders') {
      loadOrders();
    }
  }, [activeTab]);

  const TabButton = ({ id, label, isActive, onClick }) => (
    <button
      onClick={() => onClick(id)}
      className={`px-6 py-3 font-medium rounded-lg transition-colors ${
        isActive
          ? 'bg-sky-600 text-white'
          : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
      }`}
    >
      {label}
    </button>
  );

  const ProductsTab = () => (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-emerald-700">Gestion des Produits</h2>
        <button 
          onClick={() => setShowProductForm(true)}
          className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition"
        >
          Ajouter un produit
        </button>
      </div>

      {loading ? (
        <div className="text-center py-8">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-emerald-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Chargement des produits...</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {products.length > 0 ? (
            products.map((product) => (
              <div key={product.id} className="bg-white p-6 rounded-lg shadow-md border flex flex-col h-full">
                {product.imageUrl && (
                  <img 
                    src={product.imageUrl} 
                    alt={product.name}
                    className="w-full h-40 object-contain bg-gray-50 rounded-md mb-4"
                    onError={(e) => {
                      e.target.style.display = 'none';
                    }}
                  />
                )}
                <h3 className="text-lg font-semibold text-gray-800 mb-2">{product.name}</h3>
                <p className="text-gray-600 mb-2 text-sm">{product.description}</p>
                <p className="text-xl font-bold text-emerald-600 mb-2">{product.price} MAD</p>
                <p className="text-sm text-gray-500 mb-2">Catégorie: {product.category}</p>
                <p className={`text-sm mb-4 ${product.stockQuantity > 10 ? 'text-green-600' : product.stockQuantity > 0 ? 'text-orange-600' : 'text-red-600'}`}>
                  Stock: {product.stockQuantity}
                </p>
                <div className="flex space-x-2 mt-auto">
                  <button 
                    onClick={() => {
                      setEditingProduct(product);
                      setShowProductForm(true);
                    }}
                    className="bg-blue-500 text-white px-3 py-1 rounded text-sm hover:bg-blue-600 transition"
                  >
                    Modifier
                  </button>
                  <button 
                    onClick={() => handleDeleteProduct(product.id)}
                    className="bg-red-500 text-white px-3 py-1 rounded text-sm hover:bg-red-600 transition"
                  >
                    Supprimer
                  </button>
                </div>
              </div>
            ))
          ) : (
            <div className="col-span-full text-center py-8">
              <p className="text-gray-500">Aucun produit trouvé</p>
            </div>
          )}
        </div>
      )}
    </div>
  );

  const OrdersTab = () => (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-emerald-700">Gestion des Commandes</h2>

      {loading ? (
        <div className="text-center py-8">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-emerald-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Chargement des commandes...</p>
        </div>
      ) : (
        <div className="bg-white rounded-lg shadow-md overflow-hidden">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  ID Commande
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Client
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Total
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Statut
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Date
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {orders.length > 0 ? (
                orders.map((order) => (
                  <tr key={order.id}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      #{order.id}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {order.user?.name || 'N/A'}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {order.totalAmount} €
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <select
                        value={order.status}
                        onChange={(e) => handleUpdateOrderStatus(order.id, e.target.value)}
                        className={`px-2 py-1 text-xs font-semibold rounded-full border ${
                          order.status === 'COMPLETED' ? 'bg-green-100 text-green-800 border-green-200' :
                          order.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800 border-yellow-200' :
                          order.status === 'PROCESSING' ? 'bg-blue-100 text-blue-800 border-blue-200' :
                          'bg-red-100 text-red-800 border-red-200'
                        }`}
                      >
                        <option value="PENDING">En attente</option>
                        <option value="PROCESSING">En cours</option>
                        <option value="COMPLETED">Terminée</option>
                        <option value="CANCELLED">Annulée</option>
                      </select>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {new Date(order.createdAt).toLocaleDateString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <button className="text-indigo-600 hover:text-indigo-900">
                        Voir détails
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="6" className="px-6 py-4 text-center text-gray-500">
                    Aucune commande trouvée
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );

  return (
    <div className="min-h-screen bg-gray-100">
      <div className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="py-6 text-center">
            <h1 className="text-3xl font-bold text-sky-700">Management</h1>
            <p className="mt-2 text-gray-700 font-semibold">
              Bienvenue, {user?.name}. Gérez vos produits et commandes.
            </p>
            {/* Statistiques produits et commandes */}
            <div className="flex flex-wrap justify-center gap-8 mt-6">
              <div className="bg-emerald-50 border border-emerald-200 rounded-xl px-8 py-4 flex flex-col items-center shadow">
                <span className="text-2xl font-bold text-emerald-700">{products.length}</span>
                <span className="text-gray-700 font-medium mt-1">Produits présents</span>
              </div>
              <div className="bg-blue-50 border border-blue-200 rounded-xl px-8 py-4 flex flex-col items-center shadow">
                <span className="text-2xl font-bold text-sky-700">{orders.length}</span>
                <span className="text-gray-700 font-medium mt-1">Commandes reçues</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Onglets */}
        <div className="flex space-x-4 mb-8">
          <TabButton
            id="products"
            label="Produits"
            isActive={activeTab === 'products'}
            onClick={setActiveTab}
          />
          <TabButton
            id="orders"
            label="Commandes"
            isActive={activeTab === 'orders'}
            onClick={setActiveTab}
          />
        </div>

        {/* Contenu des onglets */}
        {activeTab === 'products' && <ProductsTab />}
        {activeTab === 'orders' && <OrdersTab />}
      </div>

      {/* Modal de formulaire produit */}
      {showProductForm && (
        <ProductForm
          product={editingProduct}
          onSubmit={editingProduct ? handleEditProduct : handleAddProduct}
          onCancel={() => {
            setShowProductForm(false);
            setEditingProduct(null);
          }}
          isEditing={!!editingProduct}
        />
      )}
    </div>
  );
};

export default ManagerPage;
