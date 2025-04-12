import { createContext, useState, useContext, useEffect } from "react";
import { authAPI, userAPI } from "../services/api";

// Create the auth context
const AuthContext = createContext();

// Custom hook to use the auth context
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

// Auth provider component
export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Check if user is already logged in (from localStorage)
  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      try {
        const user = JSON.parse(storedUser);
        setCurrentUser(user);
        // Update user status to online
        updateUserStatus(user.id).catch(console.error);
      } catch (err) {
        console.error("Failed to parse stored user:", err);
        localStorage.removeItem("user");
      }
    }
    setLoading(false);
  }, []);

  // Update user online status
  const updateUserStatus = async (userId) => {
    try {
      await userAPI.updateUserStatus(userId);
    } catch (err) {
      console.error("Failed to update user status:", err);
    }
  };

  // Login function
  const login = async (username, password) => {
    setError(null);
    try {
      setLoading(true);
      const response = await authAPI.login({ username, password });
      const user = response.data;

      // Save user to state and localStorage
      setCurrentUser(user);
      localStorage.setItem("user", JSON.stringify(user));

      // Update user status to online
      await updateUserStatus(user.id);

      return user;
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "로그인에 실패했습니다.";
      setError(errorMessage);
      throw new Error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Register function
  const register = async (userData, profileImage) => {
    setError(null);
    try {
      setLoading(true);
      const response = await userAPI.createUser(userData, profileImage);
      return response.data;
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "회원가입에 실패했습니다.";
      setError(errorMessage);
      throw new Error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Logout function
  const logout = async () => {
    if (currentUser) {
      try {
        // Update user status to offline before logging out
        await updateUserStatus(currentUser.id);
      } catch (err) {
        console.error("Failed to update user status on logout:", err);
      }
    }

    // Clear user from state and localStorage
    setCurrentUser(null);
    localStorage.removeItem("user");
  };

  // Update user profile
  const updateProfile = async (userId, userData) => {
    setError(null);
    try {
      setLoading(true);
      const response = await userAPI.updateUser(userId, userData);
      const updatedUser = response.data;

      // Update current user if it's the same user
      if (currentUser && currentUser.id === userId) {
        setCurrentUser(updatedUser);
        localStorage.setItem("user", JSON.stringify(updatedUser));
      }

      return updatedUser;
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "프로필 업데이트에 실패했습니다.";
      setError(errorMessage);
      throw new Error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Value to be provided by the context
  const value = {
    currentUser,
    loading,
    error,
    login,
    register,
    logout,
    updateProfile,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export default AuthContext;
