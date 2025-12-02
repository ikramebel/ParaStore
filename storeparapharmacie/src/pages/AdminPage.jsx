import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';

const AdminPage = () => {
  const { user } = useAuth();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showAddUser, setShowAddUser] = useState(false);
  const [newUser, setNewUser] = useState({
    name: '',
    email: '',
    password: '',
    role: 'USER'
  });

  // Ajout : états pour produits et commandes
  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);

  // Fonction pour récupérer le token
  const getAuthHeaders = () => {
    const token = localStorage.getItem('token');
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    };
  };

  // Charger les utilisateurs
  const loadUsers = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/admin/users', {
        headers: getAuthHeaders()
      });
      if (response.ok) {
        const data = await response.json();
        setUsers(data);
      }
    } catch (error) {
      console.error('Erreur lors du chargement des utilisateurs:', error);
    } finally {
      setLoading(false);
    }
  };

  // Charger les produits
  const loadProducts = async () => {
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
    }
  };

  // Charger les commandes
  const loadOrders = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/orders', {
        headers: getAuthHeaders()
      });
      if (response.ok) {
        const data = await response.json();
        setOrders(data);
      }
    } catch (error) {
      console.error('Erreur lors du chargement des commandes:', error);
    }
  };

  // Modifier le rôle d'un utilisateur
  const updateUserRole = async (userId, newRole) => {
    try {
      const response = await fetch(`http://localhost:8080/api/admin/users/${userId}/role`, {
        method: 'PUT',
        headers: getAuthHeaders(),
        body: JSON.stringify({ role: newRole })
      });
      
      if (response.ok) {
        loadUsers(); // Recharger la liste
      }
    } catch (error) {
      console.error('Erreur lors de la modification du rôle:', error);
    }
  };

  // Supprimer un utilisateur
  const deleteUser = async (userId) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
      try {
        const response = await fetch(`http://localhost:8080/api/admin/users/${userId}`, {
          method: 'DELETE',
          headers: getAuthHeaders()
        });
        
        if (response.ok) {
          loadUsers(); // Recharger la liste
        }
      } catch (error) {
        console.error('Erreur lors de la suppression:', error);
      }
    }
  };

  // Ajouter un nouvel utilisateur
  const addUser = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/api/admin/users', {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify(newUser)
      });
      
      if (response.ok) {
        setNewUser({ name: '', email: '', password: '', role: 'USER' });
        setShowAddUser(false);
        loadUsers(); // Recharger la liste
      }
    } catch (error) {
      console.error('Erreur lors de l\'ajout de l\'utilisateur:', error);
    }
  };

  useEffect(() => {
    loadUsers();
    loadProducts();
    loadOrders();
  }, []);

  const getRoleBadgeColor = (role) => {
    switch (role) {
      case 'ADMIN':
        return 'bg-red-100 text-red-800';
      case 'MANAGER':
        return 'bg-blue-100 text-blue-800';
      case 'USER':
      default:
        return 'bg-green-100 text-green-800';
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <div className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="py-6 text-center">
            <h1 className="text-3xl font-bold text-red-700">Administration</h1>
            <p className="mt-2 text-gray-700 font-semibold">
              Bienvenue, {user?.name}. Gérez les comptes utilisateurs.
            </p>
            {/* Statistiques utilisateurs, produits, commandes */}
            <div className="flex flex-wrap justify-center gap-8 mt-6">
              <div className="bg-emerald-50 border border-emerald-200 rounded-xl px-8 py-4 flex flex-col items-center shadow">
                <span className="text-2xl font-bold text-emerald-700">{users.length}</span>
                <span className="text-gray-700 font-medium mt-1">Utilisateurs</span>
              </div>
              <div className="bg-blue-50 border border-blue-200 rounded-xl px-8 py-4 flex flex-col items-center shadow">
                <span className="text-2xl font-bold text-sky-700">{products.length}</span>
                <span className="text-gray-700 font-medium mt-1">Produits</span>
              </div>
              <div className="bg-orange-50 border border-orange-200 rounded-xl px-8 py-4 flex flex-col items-center shadow">
                <span className="text-2xl font-bold text-orange-600">{orders.length}</span>
                <span className="text-gray-700 font-medium mt-1">Commandes</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Header avec bouton d'ajout */}
        <div className="flex justify-between items-center mb-8">
          <h2 className="text-2xl font-bold text-emerald-700">Gestion des Utilisateurs</h2>
          <button
            onClick={() => setShowAddUser(true)}
            className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition"
          >
            Ajouter un utilisateur
          </button>
        </div>

        {/* Modal d'ajout d'utilisateur */}
        {showAddUser && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded-lg w-full max-w-md">
              <h3 className="text-lg font-semibold mb-4">Ajouter un utilisateur</h3>
              <form onSubmit={addUser} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Nom</label>
                  <input
                    type="text"
                    required
                    value={newUser.name}
                    onChange={(e) => setNewUser({ ...newUser, name: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-sky-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                  <input
                    type="email"
                    required
                    value={newUser.email}
                    onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-sky-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Mot de passe</label>
                  <input
                    type="password"
                    required
                    value={newUser.password}
                    onChange={(e) => setNewUser({ ...newUser, password: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-sky-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Rôle</label>
                  <select
                    value={newUser.role}
                    onChange={(e) => setNewUser({ ...newUser, role: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-sky-500"
                  >
                    <option value="USER">Client</option>
                    <option value="MANAGER">Manager</option>
                    <option value="ADMIN">Admin</option>
                  </select>
                </div>
                <div className="flex space-x-3 pt-4">
                  <button
                    type="submit"
                    className="flex-1 bg-sky-600 text-white py-2 rounded-md hover:bg-sky-700 transition"
                  >
                    Ajouter
                  </button>
                  <button
                    type="button"
                    onClick={() => setShowAddUser(false)}
                    className="flex-1 bg-gray-300 text-gray-700 py-2 rounded-md hover:bg-gray-400 transition"
                  >
                    Annuler
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        {/* Liste des utilisateurs */}
        {loading ? (
          <div className="text-center py-8">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-sky-600 mx-auto"></div>
            <p className="mt-4 text-gray-600">Chargement des utilisateurs...</p>
          </div>
        ) : (
          <div className="bg-white rounded-lg shadow-md overflow-hidden">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    ID
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Nom
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Email
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Rôle
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Date d'inscription
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {users.length > 0 ? (
                  users.map((userItem) => (
                    <tr key={userItem.id}>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {userItem.id}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {userItem.name}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {userItem.email}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getRoleBadgeColor(userItem.role)}`}>
                          {userItem.role}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {new Date(userItem.createdAt).toLocaleDateString()}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                        <select
                          value={userItem.role}
                          onChange={(e) => updateUserRole(userItem.id, e.target.value)}
                          className="text-xs border border-gray-300 rounded px-2 py-1"
                          disabled={userItem.id === user?.id} // Empêcher de modifier son propre rôle
                        >
                          <option value="USER">Client</option>
                          <option value="MANAGER">Manager</option>
                          <option value="ADMIN">Admin</option>
                        </select>
                        {userItem.id !== user?.id && (
                          <button
                            onClick={() => deleteUser(userItem.id)}
                            className="text-red-600 hover:text-red-900 text-xs"
                          >
                            Supprimer
                          </button>
                        )}
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="6" className="px-6 py-4 text-center text-gray-500">
                      Aucun utilisateur trouvé
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default AdminPage;