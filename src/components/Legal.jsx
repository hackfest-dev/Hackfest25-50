import React from 'react';
import { Link } from 'react-router-dom';
import Navbar from './Navbar';

const Legal = () => {
  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-4xl font-bold text-purple-800 mb-8">Legal Resources & Support</h1>
        
        <div className="grid md:grid-cols-2 gap-8">
          <div className="bg-white rounded-lg shadow-lg p-6">
            <h2 className="text-2xl font-semibold text-purple-700 mb-4">AI Legal Assistant</h2>
            <p className="text-gray-600 mb-4">
              Get immediate guidance on your legal rights and available support services. Our AI-powered
              legal assistant can help you understand your rights and recommend appropriate actions.
            </p>
            <Link
              to="/legal-chat"
              className="inline-block bg-purple-600 text-white px-6 py-3 rounded-lg hover:bg-purple-700 transition-colors"
            >
              Chat with Legal Assistant
            </Link>
          </div>

   

       

          <div className="bg-white rounded-lg shadow-lg p-6">
            <h2 className="text-2xl font-semibold text-purple-700 mb-4">Common Legal Issues</h2>
            <ul className="space-y-3">
              <li className="flex items-center">
                <span className="mr-2">🏠</span>
                Domestic Violence
              </li>
              <li className="flex items-center">
                <span className="mr-2">💼</span>
                Workplace Harassment
              </li>
              <li className="flex items-center">
                <span className="mr-2">📱</span>
                Cybercrime
              </li>
              <li className="flex items-center">
                <span className="mr-2">⚖️</span>
                Property Rights
              </li>
              <li className="flex items-center">
                <span className="mr-2">👶</span>
                Child Custody
              </li>
              <li className="flex items-center">
                <span className="mr-2">💍</span>
                Divorce Matters
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Legal;