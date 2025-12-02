import React from "react";

export default function Footer() {
  return (
    <footer className="bg-gray-800 text-gray-200 pt-10 pb-6 px-6">
      <div className="max-w-7xl mx-auto grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-8">

        {/* À propos */}
        <div>
          <h3 className="text-lg font-semibold mb-4 text-white">À propos</h3>
          <p className="text-sm">
            Notre parapharmacie en ligne vous propose une large gamme de produits de qualité pour votre bien-être et santé, livrés chez vous en toute sécurité.
          </p>
        </div>

        {/* Liens rapides */}
        <div>
          <h3 className="text-lg font-semibold mb-4 text-white">Liens utiles</h3>
          <ul className="text-sm space-y-2">
            <li><a href="/" className="hover:text-white">Accueil</a></li>
            <li><a href="/products" className="hover:text-white">Nos Produits</a></li>
            <li><a href="#services" className="hover:text-white">Nos Services</a></li>
            <li><a href="#faq" className="hover:text-white">FAQ</a></li>
          </ul>
        </div>

        {/* Contact */}
        <div>
          <h3 className="text-lg font-semibold mb-4 text-white">Contact</h3>
          <ul className="text-sm space-y-2">
            <li>📍 Agadir, Maroc</li>
            <li>📞 +212 6 12 34 56 78</li>
            <li>✉️ contact@parastore.ma</li>
          </ul>
        </div>

        {/* Réseaux sociaux */}
        <div>
          <h3 className="text-lg font-semibold mb-4 text-white">Suivez-nous</h3>
          <div className="flex space-x-4 text-xl">
            <a href="#" className="hover:text-white">🌐</a>
            <a href="#" className="hover:text-white">📘</a>
            <a href="#" className="hover:text-white">📸</a>
            <a href="#" className="hover:text-white">🐦</a>
          </div>
        </div>
      </div>

      {/* Bas de page */}
      <div className="mt-10 border-t border-gray-700 pt-4 text-center text-sm text-gray-400">
        &copy; {new Date().getFullYear()} ParaStore. Tous droits réservés.
      </div>
    </footer>
  );
}
