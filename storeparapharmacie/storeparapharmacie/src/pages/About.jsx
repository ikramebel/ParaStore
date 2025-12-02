import React from 'react';

export default function About() {
  return (
    <div className="min-h-screen bg-gray-50 py-12 px-6">
      <div className="max-w-6xl mx-auto space-y-16">

        {/* Titre principal */}
        <div className="text-center">
          <h1 className="text-4xl font-extrabold text-teal-600 mb-4">À propos de Parastore</h1>
          <p className="text-gray-700 text-lg">
            Votre parapharmacie en ligne de confiance, au service de votre santé et de votre bien-être.
          </p>
        </div>

        {/* Image + mission */}
        <div className="flex flex-col md:flex-row items-center gap-10">
          <img
            src="/hero-image.svg"
            alt="Parapharmacie illustration"
            className="w-full md:w-1/2 rounded-lg shadow-lg"
          />
          <div>
            <h2 className="text-2xl font-bold text-gray-800 mb-3">Notre mission</h2>
            <p className="text-gray-600 leading-relaxed">
              Chez <strong>Parastore</strong>, nous nous engageons à rendre les produits de parapharmacie
              accessibles à tous, avec des conseils fiables, une livraison rapide, et un service client à l’écoute.
              Nous croyons que la santé et le bien-être doivent être simples, sécurisés et accessibles en ligne.
            </p>
          </div>
        </div>

        {/* Valeurs */}
        <div>
          <h2 className="text-2xl font-bold text-gray-800 mb-6 text-center">Nos Valeurs</h2>
          <div className="grid md:grid-cols-3 gap-8">
            <div className="bg-white p-6 rounded shadow hover:shadow-md transition">
              <h3 className="text-xl font-semibold text-teal-600 mb-2">Qualité</h3>
              <p className="text-gray-600">
                Tous nos produits sont sélectionnés auprès de laboratoires reconnus pour leur sérieux et leur efficacité.
              </p>
            </div>
            <div className="bg-white p-6 rounded shadow hover:shadow-md transition">
              <h3 className="text-xl font-semibold text-teal-600 mb-2">Transparence</h3>
              <p className="text-gray-600">
                Nous vous informons clairement sur les composants, les prix et l’origine des produits.
              </p>
            </div>
            <div className="bg-white p-6 rounded shadow hover:shadow-md transition">
              <h3 className="text-xl font-semibold text-teal-600 mb-2">Écoute</h3>
              <p className="text-gray-600">
                Notre service client est à votre disposition pour toute question ou recommandation personnalisée.
              </p>
            </div>
          </div>
        </div>

        {/* Confiance */}
        <div className="bg-white p-8 rounded-lg shadow-lg text-center">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">Pourquoi nous faire confiance ?</h2>
          <p className="text-gray-600 max-w-3xl mx-auto">
            Plus de <strong>1000 clients satisfaits</strong> à travers le Maroc, des livraisons rapides,
            un large choix de produits authentiques et un accompagnement professionnel. Faites le bon choix
            pour votre santé : choisissez <span className="text-teal-600 font-semibold">Parastore</span>.
          </p>
        </div>
      </div>
    </div>
  );
}
