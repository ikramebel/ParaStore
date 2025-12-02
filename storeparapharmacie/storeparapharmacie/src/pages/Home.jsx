export default function Home() {
  return (
    <>
      {/* Partie d'Accueil */}
      <section className="bg-purple-50 py-16 px-6 md:px-20">
        <div className="flex flex-col-reverse md:flex-row items-center justify-between gap-10">
          {/* Texte */}
          <div className="md:w-1/2 text-center md:text-left">
            <h1 className="text-4xl md:text-5xl font-bold text-gray-800 leading-tight mb-4">
              Simplifiez vos achats en ligne <br />
              avec <span className="text-teal-500">Parastore</span>
            </h1>
            <p className="text-gray-600 mb-8 text-lg">
              Notre parapharmacie en ligne vous permet de commander facilement vos produits préférés sans vous déplacer. Livraison rapide et gratuite.
            </p>

            <div className="flex flex-col sm:flex-row gap-4 justify-center md:justify-start">
              <button
                onClick={() => {
                  document.getElementById('services')?.scrollIntoView({ behavior: 'smooth' });
                }}
                className="bg-teal-500 text-white px-6 py-3 rounded-full shadow hover:bg-teal-600 transition"
              >
                Nos Services
              </button>

              <button
                onClick={() => window.location.href = "/products"}
                className="bg-white border border-teal-500 text-teal-500 px-6 py-3 rounded-full hover:bg-teal-100 transition"
              >
                Commander
              </button>
            </div>
          </div>

          {/* Illustration */}
          <div className="md:w-1/2">
            <img
              src="/hero-image.svg"
              alt="Illustration de la parapharmacie"
              className="w-full max-w-lg mx-auto"
            />
          </div>
        </div>
      </section>

      {/* Section Services */}
      <section id="services" className="py-16 bg-gray-100">
        <div className="max-w-6xl mx-auto px-4 text-center">
          <h2 className="text-4xl font-extrabold text-gray-900 mb-8 tracking-wide drop-shadow-sm">Nos Services</h2>
          <p className="text-gray-600 mb-12">
            Découvrez les services de notre parapharmacie conçus pour vous offrir une expérience d’achat en ligne simple, rapide et fiable.
          </p>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {/* Service 1 */}
            <div className="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition">
              <div className="mb-4">
                <img src="/vite.svg" alt="Livraison" className="mx-auto w-12 h-12" />
              </div>
              <h3 className="text-xl font-semibold text-sky-600 mb-2">Livraison Gratuite</h3>
              <p className="text-gray-600">
                Recevez vos produits rapidement partout au Maroc sans frais supplémentaires.
              </p>
            </div>

            {/* Service 2 */}
            <div className="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition">
              <div className="mb-4">
                <img src="/vite.svg" alt="Conseils" className="mx-auto w-12 h-12" />
              </div>
              <h3 className="text-xl font-semibold text-sky-600 mb-2">Conseils Santé</h3>
              <p className="text-gray-600">
                Accédez à des recommandations fiables pour mieux utiliser vos produits.
              </p>
            </div>

            {/* Service 3 */}
            <div className="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition">
              <div className="mb-4">
                <img src="/vite.svg" alt="Paiement" className="mx-auto w-12 h-12" />
              </div>
              <h3 className="text-xl font-semibold text-sky-600 mb-2">Paiement Sécurisé</h3>
              <p className="text-gray-600">
                Payez en toute confiance avec nos solutions de paiement sécurisées.
              </p>
            </div>
          </div>
        </div>
      </section>

      <section className="bg-gradient-to-r from-teal-500 to-sky-500 text-white py-8 text-center">
  <h2 className="text-2xl font-bold mb-2">Offre spéciale d'été !</h2>
  <p className="text-lg">Jusqu’à <span className="font-semibold">-30%</span> sur les soins du visage.</p>
    </section>

      <section className="py-16 bg-white text-center">
  <h2 className="text-3xl font-bold text-gray-800 mb-10">Explorez nos Catégories</h2>
  <div className="grid grid-cols-1 sm:grid-cols-3 gap-8 max-w-6xl mx-auto">
    {[
      { label: "Soins du visage", icon: "🧴", query: "visage" },
      { label: "Compléments alimentaires", icon: "💊", query: "bienetre" },
      { label: "Hygiène corporelle", icon: "🧼", query: "hygiene" },
    ].map((cat, i) => (
      <div
        key={i}
        className="bg-gray-100 p-6 rounded shadow hover:shadow-md cursor-pointer transition"
        onClick={() => (window.location.href = `/products?category=${cat.query}`)}
      >
        <div className="text-4xl mb-4">{cat.icon}</div>
        <h3 className="text-lg font-semibold">{cat.label}</h3>
      </div>
    ))}
  </div>
</section>


        <section className="py-16 bg-gray-50 text-center">
  <h2 className="text-3xl font-bold text-gray-800 mb-10">Avis de nos Clients</h2>
  <div className="grid md:grid-cols-3 gap-6 max-w-6xl mx-auto">
    {[
      {
        name: "Leila - Agadir",
        text: "Livraison rapide et produits de qualité. Je recommande vivement !",
      },
      {
        name: "Youssef - Casablanca",
        text: "Navigation simple, commande facile, service client au top.",
      },
      {
        name: "Nadia - Marrakech",
        text: "Parfait pour trouver mes compléments sans me déplacer.",
      },
    ].map((avis, i) => (
      <div key={i} className="bg-white shadow-md p-6 rounded">
        <p className="text-gray-700 italic mb-4">"{avis.text}"</p>
        <p className="font-semibold text-sky-600">⭐️⭐️⭐️⭐️⭐️</p>
        <p className="text-gray-500 text-sm mt-2">{avis.name}</p>
      </div>
    ))}
  </div>
    </section>

        <section className="py-16 bg-white text-center">
  <h2 className="text-3xl font-bold text-gray-800 mb-10">Conseils Santé</h2>
  <div className="grid md:grid-cols-3 gap-6 max-w-6xl mx-auto">
    {[
      {
        title: "Pourquoi prendre du magnésium ?",
        desc: "Découvrez les bienfaits du magnésium sur le stress et la fatigue.",
      },
      {
        title: "Bien hydrater sa peau",
        desc: "Nos conseils pour une peau douce et bien nourrie toute l’année.",
      },
      {
        title: "Les probiotiques : alliés digestifs",
        desc: "Améliorez votre flore intestinale avec des produits adaptés.",
      },
    ].map((post, i) => (
      <div key={i} className="bg-gray-100 p-6 rounded-lg shadow hover:shadow-md transition">
        <h3 className="text-xl font-semibold text-sky-600 mb-2">{post.title}</h3>
        <p className="text-gray-600 text-sm">{post.desc}</p>
      </div>
    ))}
  </div>
    </section>


        <section className="py-12 bg-gray-100">
  <h2 className="text-center text-2xl font-bold text-gray-800 mb-6">Nos Marques partenaires</h2>
  <div className="flex flex-wrap justify-center gap-10 items-center">
    {["/eucerin.png", "/nuxe.png", "/vichy.png", "/bioderma.png", "/la-roche.png"].map((src, i) => (
      <img key={i} src={src} alt="Marque" className="h-12 grayscale hover:grayscale-0 transition" />
    ))}
  </div>
    </section>

    <section className="bg-gray-50 py-16">
  <div className="max-w-4xl mx-auto px-4">
    <h2 className="text-3xl font-bold text-center text-gray-800 mb-8">Questions Fréquentes</h2>
    <div className="space-y-6">
      {[
        {
          q: "Quels sont les délais de livraison ?",
          a: "Nous livrons partout au Maroc en 24 à 48h.",
        },
        {
          q: "Comment puis-je payer ?",
          a: "Par carte bancaire, PayPal ou paiement à la livraison.",
        },
        {
          q: "Puis-je retourner un produit ?",
          a: "Oui, dans un délai de 7 jours si le produit est scellé.",
        },
      ].map((item, i) => (
        <div key={i} className="bg-white p-4 rounded shadow">
          <h3 className="font-semibold text-sky-600">{item.q}</h3>
          <p className="text-gray-700 mt-2">{item.a}</p>
        </div>
      ))}
    </div>
  </div>
    </section>

      <section className="py-16 bg-white text-center">
  <h2 className="text-3xl font-bold text-gray-800 mb-10">Pourquoi nous choisir ?</h2>
  <div className="grid sm:grid-cols-3 gap-8 max-w-5xl mx-auto">
    {[
      {
        icon: "🚚",
        title: "Livraison Express",
        desc: "Recevez vos produits en moins de 48h partout au Maroc.",
      },
      {
        icon: "🛡️",
        title: "Paiement sécurisé",
        desc: "Toutes les transactions sont protégées avec cryptage SSL.",
      },
      {
        icon: "📞",
        title: "Support réactif",
        desc: "Une équipe à votre écoute pour répondre à toutes vos questions.",
      },
    ].map((item, i) => (
      <div key={i} className="p-6 bg-gray-50 rounded shadow text-center hover:shadow-md transition">
        <div className="text-4xl mb-4">{item.icon}</div>
        <h3 className="text-xl font-semibold text-sky-600">{item.title}</h3>
        <p className="text-gray-600 text-sm mt-2">{item.desc}</p>
      </div>
    ))}
  </div>
    </section>
 
    </>
  );
}
