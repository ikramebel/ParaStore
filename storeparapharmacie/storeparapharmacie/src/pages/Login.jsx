import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useLocation, useNavigate } from 'react-router-dom';


const Login = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  // L'URL précédente ou fallback vers "/"
  const from = location.state?.from?.pathname || '/';

  const handleSubmit = (e) => {
    e.preventDefault();

    const fakeUser = {
      name: 'Taha',
      email,
    };

    login(fakeUser);
    navigate(from, { replace: true }); // Redirige vers la page précédente
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 px-4">
      <div className="max-w-md w-full bg-white p-8 rounded shadow">
        <h2 className="text-3xl font-bold text-center text-sky-700 mb-6">Connexion</h2>
        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block mb-1 text-gray-700">Adresse Email</label>
            <input
              type="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-sky-500"
            />
          </div>
          <div>
            <label className="block mb-1 text-gray-700">Mot de passe</label>
            <input
              type="password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-sky-500"
            />
          </div>
          <button
            type="submit"
            className="w-full bg-sky-600 text-white py-2 rounded hover:bg-sky-700 transition"
          >
            Se connecter
          </button>
        </form>
        <p className="text-center mt-4 text-sm text-gray-600">
          Vous n'avez pas de compte ?{' '}
          <Link to="/register" className="text-sky-600 hover:underline">
            Créer un compte
          </Link>
        </p>
      </div>
    </div>
  );
};

export default Login;
