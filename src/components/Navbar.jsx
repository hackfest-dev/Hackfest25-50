import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Home, Users, FileText, Shield } from 'lucide-react';
import SAADHANLOGO1 from '../assets/SAADHANLOGO1.png';
const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const isActive = (path) => {
    return location.pathname === path;
  };

  const navItems = [
    { path: '/', label: 'Home', icon: Home },
    { path: '/legal', label: 'Legal', icon: Shield },
    { path: '/schemes', label: 'Schemes', icon: FileText },
    { path: '/ngos', label: 'NGOs', icon: Users },
  ];

  return (
    <nav className="bg-white shadow-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          {/* Logo and Brand */}
          <div className="flex items-center cursor-pointer" onClick={() => navigate('/')}>
            <img 
              src={SAADHANLOGO1} 
              alt="SAADHAN" 
              className="h-8 w-auto mr-2"
            />
          </div>

          {/* Navigation Links */}
          <div className="hidden md:flex items-center space-x-4">
            {navItems.map((item) => {
              const Icon = item.icon;
              return (
                <button
                  key={item.path}
                  onClick={() => navigate(item.path)}
                  className={`flex items-center px-3 py-2 rounded-md text-sm font-medium transition-colors duration-200 ${
                    isActive(item.path)
                      ? 'text-blue-600 bg-blue-50'
                      : 'text-gray-600 hover:text-blue-600 hover:bg-blue-50'
                  }`}
                >
                  <Icon size={18} className="mr-1.5" />
                  {item.label}
                </button>
              );
            })}
          </div>

          {/* Mobile Navigation */}
          <div className="md:hidden fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 z-50">
            <div className="grid grid-cols-4 gap-1 p-2">
              {navItems.map((item) => {
                const Icon = item.icon;
                return (
                  <button
                    key={item.path}
                    onClick={() => navigate(item.path)}
                    className={`flex flex-col items-center py-2 px-1 rounded-md ${
                      isActive(item.path)
                        ? 'text-blue-600'
                        : 'text-gray-600 hover:text-blue-600'
                    }`}
                  >
                    <Icon size={20} />
                    <span className="text-xs mt-1">{item.label}</span>
                    {isActive(item.path) && (
                      <div className="w-1.5 h-1.5 bg-blue-600 rounded-full mt-1" />
                    )}
                  </button>
                );
              })}
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;