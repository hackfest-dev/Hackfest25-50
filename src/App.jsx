import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { WomenAwarenessHomepage } from './components/Home';
import Legal from './components/Legal';
import Government from './components/Government';
import Ngo from './components/Ngo';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import LegalChatbot from './components/LegalChatbot';
import Schemes from './components/Schemes';

const App = () => {
  return (
    <Router>
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />
      <Routes>
        <Route path="/" element={<WomenAwarenessHomepage />} />
        <Route path="/legal" element={<Legal />} />
        <Route path="/government" element={<Government />} />
        <Route path="/ngo" element={<Ngo />} />
        <Route path="/ngos" element={<Navigate to="/ngo" replace />} />
        <Route path="/schemes" element={<Schemes />} />
        <Route path="/legal-chat" element={
          <div className="min-h-screen bg-gray-100 p-4">
            <LegalChatbot />
          </div>
        } />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Router>
  );
};

export default App;
