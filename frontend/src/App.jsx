import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Login';
import Register from './components/Register';
import StyleAnalysis from './pages/StyleAnalysis';
import BodyShapeTest from './components/BodyShapeTest';
import Profile from './components/Profile';
import ForgotPassword from './pages/ForgotPassword';
import ResetPassword from './pages/ResetPassword';
import ErrorBoundary from './components/ErrorBoundary';


const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem('token');
  if (!token) return <Navigate to="/login" replace />;
  return children;
};

function App() {
  return (
    <Router>
      <div className="app-container">
        <div className="bg-glow bg-glow-1"></div>
        <div className="bg-glow bg-glow-2"></div>
        <ErrorBoundary>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/forgot-password" element={<ForgotPassword />} />
            <Route path="/reset-password" element={<ResetPassword />} />
            <Route path="/analysis" element={<ProtectedRoute><StyleAnalysis /></ProtectedRoute>} />
            <Route path="/body-shape-test" element={<ProtectedRoute><BodyShapeTest /></ProtectedRoute>} />
            <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
            <Route path="/" element={<Navigate to="/register" replace />} />
          </Routes>
        </ErrorBoundary>
      </div>
    </Router>

  );
}

export default App;
