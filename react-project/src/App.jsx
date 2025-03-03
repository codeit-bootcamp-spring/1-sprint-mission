import { useState } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { ChannelProvider } from "./context/ChannelContext";
import { MessageProvider } from "./context/MessageContext";
import { useAuth } from "./context/AuthContext";
import LoginForm from "./components/auth/LoginForm";
import RegisterForm from "./components/auth/RegisterForm";
import MainLayout from "./components/layout/MainLayout";

// Protected route component
const ProtectedRoute = ({ children }) => {
  const { currentUser, loading } = useAuth();

  if (loading) {
    return (
      <div className="flex h-screen items-center justify-center bg-gray-900">
        <div className="text-center text-white">
          <div className="mb-4 text-xl font-bold">로딩 중...</div>
          <div className="mx-auto h-8 w-8 animate-spin rounded-full border-4 border-gray-600 border-t-indigo-500"></div>
        </div>
      </div>
    );
  }

  return currentUser ? children : <Navigate to="/login" />;
};

// App component with routing
function App() {
  const [isRegistering, setIsRegistering] = useState(false);

  const handleGoRegister = () => {
    setIsRegistering(true);
  };

  const handleGoLogin = () => {
    setIsRegistering(false);
  };

  return (
    <Router>
      <AuthProvider>
        <Routes>
          <Route
            path="/login"
            element={
              isRegistering ? (
                <RegisterForm
                  onGoLogin={handleGoLogin}
                  onRegistered={handleGoLogin}
                />
              ) : (
                <LoginForm onGoRegister={handleGoRegister} />
              )
            }
          />
          <Route
            path="/"
            element={
              <ProtectedRoute>
                <ChannelProvider>
                  <MessageProvider>
                    <MainLayout />
                  </MessageProvider>
                </ChannelProvider>
              </ProtectedRoute>
            }
          />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;
