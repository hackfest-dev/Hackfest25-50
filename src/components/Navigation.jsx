import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Home, Book, Users, ArrowLeft } from 'lucide-react';

const Navigation = ({ title }) => {
  const navigate = useNavigate();

  return (
    <>
      {/* Header */}
      <div className="bg-white shadow-md">
        <div className="container mx-auto px-4 py-4 flex items-center">
          <button
            onClick={() => navigate('/')}
            className="text-gray-600 hover:text-blue-600 transition-colors"
          >
            <ArrowLeft size={24} />
          </button>
          <h1 className="text-2xl font-semibold ml-4">{title}</h1>
        </div>
      </div>

      
    </>
  );
};

export default Navigation;