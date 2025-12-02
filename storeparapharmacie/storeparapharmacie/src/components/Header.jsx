import { FaUser, FaShoppingCart } from "react-icons/fa";
import { Link } from 'react-router-dom';



function Header() {
  return (
    <header className="flex items-center justify-between px-6 py-4 shadow bg-white">
      {/* Logo */}
      <h1 className="text-2xl font-bold text-sky-600">Parastore</h1>

      {/* Navigation */}
      <nav className="flex gap-6 text-gray-700 font-medium">
        <Link to="/">Home</Link>
        <Link to="/products">Products</Link>
        <Link to="/about">About Us</Link>
        <Link to="/contact">Contact</Link>
      </nav>

      {/* Icons */}
      <div className="flex gap-4 text-sky-600 text-lg">
        <Link to="/login" className="text-sky-600 hover:text-sky-800 transition">
          <FaUser className="cursor-pointer" />
        </Link>

        <Link to="/cart" className="text-xl text-sky-700 hover:text-sky-900">
          <FaShoppingCart className="cursor-pointer" />
        </Link>

      </div>
    </header>
  );
}

export default Header;
