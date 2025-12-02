import { Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Contact from './pages/Contact';
import Products from './pages/Products';
import About from './pages/About';
import Cart from './pages/Cart';
import Login from './pages/Login';
import Register from './pages/Register';
import ManagerPage from './pages/ManagerPage';
import AdminPage from './pages/AdminPage';
import PrivateRoute from './components/PrivateRoute';
import RoleBasedRoute from './components/RoleBasedRoute';
import Header from './components/Header';
import Footer from './components/Footer';
import { useAuth } from './context/AuthContext';

function App() {
  const { loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-sky-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Chargement de l'application...</p>
        </div>
      </div>
    );
  }

  return (
    <>
      <Header />
      <main className="mt-6">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/about" element={<About />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          
          {/* Routes protégées pour tous les utilisateurs connectés */}
          <Route
            path="/products"
            element={
              <PrivateRoute>
                <Products />
              </PrivateRoute>
            }
          />
          <Route
            path="/cart"
            element={
              <PrivateRoute>
                <Cart />
              </PrivateRoute>
            }
          />

          {/* Routes spécifiques aux rôles */}
          <Route
            path="/manager"
            element={
              <RoleBasedRoute allowedRoles={['MANAGER']}>
                <ManagerPage />
              </RoleBasedRoute>
            }
          />
          <Route
            path="/admin"
            element={
              <RoleBasedRoute allowedRoles={['ADMIN']}>
                <AdminPage />
              </RoleBasedRoute>
            }
          />
        </Routes>
      </main>
      <Footer />
    </>
  );
}

export default App;