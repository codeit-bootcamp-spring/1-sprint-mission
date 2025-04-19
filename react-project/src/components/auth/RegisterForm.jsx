import { useState, useRef } from "react";
import { useAuth } from "../../context/AuthContext";

const RegisterForm = ({ onGoLogin, onRegistered }) => {
  const { register, loading, error } = useAuth();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [profileImage, setProfileImage] = useState(null);
  const [localError, setLocalError] = useState("");
  const fileInputRef = useRef(null);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      if (file.size > 5 * 1024 * 1024) {
        // 5MB limit
        setLocalError("프로필 이미지는 5MB 이하여야 합니다.");
        fileInputRef.current.value = "";
        return;
      }

      if (!file.type.startsWith("image/")) {
        setLocalError("이미지 파일만 업로드 가능합니다.");
        fileInputRef.current.value = "";
        return;
      }

      setProfileImage(file);
      setLocalError("");
    }
  };

  const validateForm = () => {
    if (!username.trim()) {
      setLocalError("사용자명을 입력해주세요.");
      return false;
    }

    if (username.length < 2 || username.length > 6) {
      setLocalError("사용자명은 2~6자 사이여야 합니다.");
      return false;
    }

    if (!email.trim()) {
      setLocalError("이메일을 입력해주세요.");
      return false;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setLocalError("유효한 이메일 주소를 입력해주세요.");
      return false;
    }

    if (!password) {
      setLocalError("비밀번호를 입력해주세요.");
      return false;
    }

    if (password.length < 6) {
      setLocalError("비밀번호는 최소 6자 이상이어야 합니다.");
      return false;
    }

    if (password !== confirmPassword) {
      setLocalError("비밀번호가 일치하지 않습니다.");
      return false;
    }

    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLocalError("");

    if (!validateForm()) {
      return;
    }

    try {
      const userData = {
        username,
        email,
        password,
      };

      await register(userData, profileImage);

      // Clear form
      setUsername("");
      setEmail("");
      setPassword("");
      setConfirmPassword("");
      setProfileImage(null);
      if (fileInputRef.current) {
        fileInputRef.current.value = "";
      }

      // Notify parent component
      if (onRegistered) {
        onRegistered();
      }

      // Go to login page
      onGoLogin();
    } catch (err) {
      // Error is handled by the auth context
      console.error("Registration error:", err);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-900 px-4 py-12 sm:px-6 lg:px-8">
      <div className="w-full max-w-md space-y-8 rounded-lg bg-gray-800 p-8 shadow-lg">
        <div>
          <h2 className="mt-6 text-center text-3xl font-bold tracking-tight text-white">
            Discodeit 계정 만들기
          </h2>
        </div>

        {(error || localError) && (
          <div className="rounded-md bg-red-500 bg-opacity-10 p-3 text-sm text-red-400">
            {error || localError}
          </div>
        )}

        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="space-y-4">
            <div>
              <label
                htmlFor="username"
                className="block text-sm font-medium text-gray-300"
              >
                사용자명
              </label>
              <input
                id="username"
                name="username"
                type="text"
                required
                className="input mt-1 block w-full"
                placeholder="2~6자 사이"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>

            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium text-gray-300"
              >
                이메일
              </label>
              <input
                id="email"
                name="email"
                type="email"
                required
                className="input mt-1 block w-full"
                placeholder="example@email.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>

            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-gray-300"
              >
                비밀번호
              </label>
              <input
                id="password"
                name="password"
                type="password"
                required
                className="input mt-1 block w-full"
                placeholder="최소 6자 이상"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>

            <div>
              <label
                htmlFor="confirmPassword"
                className="block text-sm font-medium text-gray-300"
              >
                비밀번호 확인
              </label>
              <input
                id="confirmPassword"
                name="confirmPassword"
                type="password"
                required
                className="input mt-1 block w-full"
                placeholder="비밀번호 재입력"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
            </div>

            <div>
              <label
                htmlFor="profileImage"
                className="block text-sm font-medium text-gray-300"
              >
                프로필 이미지 (선택사항)
              </label>
              <input
                id="profileImage"
                name="profileImage"
                type="file"
                accept="image/*"
                ref={fileInputRef}
                onChange={handleFileChange}
                className="mt-1 block w-full rounded-md border border-gray-700 bg-gray-800 px-3 py-2 text-sm text-white file:mr-4 file:rounded-md file:border-0 file:bg-indigo-600 file:px-4 file:py-2 file:text-sm file:font-medium file:text-white hover:file:bg-indigo-700"
              />
              <p className="mt-1 text-xs text-gray-400">
                최대 5MB, 이미지 파일만 가능
              </p>
            </div>
          </div>

          <div>
            <button
              type="submit"
              disabled={loading}
              className="btn-primary btn group relative flex w-full justify-center"
            >
              {loading ? "가입 처리 중..." : "가입하기"}
            </button>
          </div>

          <div className="text-center text-sm">
            <p className="text-gray-400">
              이미 계정이 있으신가요?{" "}
              <button
                type="button"
                onClick={onGoLogin}
                className="font-medium text-indigo-400 hover:text-indigo-300"
              >
                로그인
              </button>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
};

export default RegisterForm;
