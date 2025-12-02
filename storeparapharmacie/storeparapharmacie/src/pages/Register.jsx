import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Register() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    nom: '',
    email: '',
    password: '',
    confirmPassword: ''
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (form.password !== form.confirmPassword) {
      alert("Les mots de passe ne correspondent pas !");
      return;
    }

    // Ici tu pourras plus tard appeler ton backend pour enregistrer l'utilisateur
    console.log('Formulaire envoyé :', form);
    alert("Inscription réussie !");
    navigate('/login');
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 px-4">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h2 className="text-2xl font-bold text-center mb-6">Créer un compte</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            name="nom"
            placeholder="Nom complet"
            value={form.nom}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-sky-500"
            required
          />
          <input
            type="email"
            name="email"
            placeholder="Adresse e-mail"
            value={form.email}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-sky-500"
            required
          />
          <input
            type="password"
            name="password"
            placeholder="Mot de passe"
            value={form.password}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-sky-500"
            required
          />
          <input
            type="password"
            name="confirmPassword"
            placeholder="Confirmez le mot de passe"
            value={form.confirmPassword}
            onChange={handleChange}
            className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-sky-500"
            required
          />
          <button
            type="submit"
            className="w-full bg-sky-600 hover:bg-sky-700 text-white py-2 rounded-lg font-semibold transition"
          >
            S'inscrire
          </button>
        </form>
        <p className="text-sm text-center mt-4">
          Déjà un compte ? <a href="/login" className="text-sky-600 hover:underline">Se connecter</a>
        </p>
      </div>
    </div>
  );
}
