import React, { useState } from 'react';

export default function Contact() {
  const [formData, setFormData] = useState({
    nom: '',
    email: '',
    message: '',
  });

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    alert('Message envoyé avec succès !');
    // Ici tu peux faire un POST vers ton backend plus tard
    setFormData({ nom: '', email: '', message: '' });
  };

  return (
    <div className="min-h-screen bg-gray-50 py-10 px-4">
      <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-10">
        {/* Formulaire */}
        <div className="bg-white p-8 rounded shadow">
          <h2 className="text-3xl font-bold text-teal-600 mb-6">Contactez-nous</h2>
          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label className="block mb-1 font-semibold">Nom complet</label>
              <input
                type="text"
                name="nom"
                value={formData.nom}
                onChange={handleChange}
                required
                className="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-teal-500"
              />
            </div>
            <div>
              <label className="block mb-1 font-semibold">Adresse email</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
                className="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-teal-500"
              />
            </div>
            <div>
              <label className="block mb-1 font-semibold">Message</label>
              <textarea
                name="message"
                value={formData.message}
                onChange={handleChange}
                required
                rows="5"
                className="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-teal-500"
              />
            </div>
            <button
              type="submit"
              className="bg-teal-500 hover:bg-teal-600 text-white font-semibold px-6 py-2 rounded transition"
            >
              Envoyer
            </button>
          </form>
        </div>

        {/* Coordonnées */}
        <div className="bg-white p-8 rounded shadow space-y-6">
          <h2 className="text-2xl font-bold text-teal-600">Nos coordonnées</h2>
          <p className="text-gray-700">N’hésitez pas à nous contacter pour toute question ou information.</p>
          <div className="space-y-3">
            <div>
              <span className="font-semibold">📍 Adresse :</span>
              <p>123 Boulevard Santé, Casablanca, Maroc</p>
            </div>
            <div>
              <span className="font-semibold">📞 Téléphone :</span>
              <p>+212 6 00 00 00 00</p>
            </div>
            <div>
              <span className="font-semibold">✉️ Email :</span>
              <p>contact@parastore.ma</p>
            </div>
            <div>
              <span className="font-semibold">🕐 Horaires :</span>
              <p>Lundi - Vendredi : 9h - 18h</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
