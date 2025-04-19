import { useState } from "react";
import { useAuth } from "../../context/AuthContext";

const LoginForm = ({ onGoRegister }) => {
  const { login, loading, error } = useAuth();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [localError, setLocalError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLocalError("");

    // Validate inputs
    if (!username.trim()) {
      setLocalError("사용자명을 입력해주세요.");
      return;
    }

    if (!password) {
      setLocalError("비밀번호를 입력해주세요.");
      return;
    }

    try {
      await login(username, password);
    } catch (err) {
      // Error is handled by the auth context
      console.error("Login error:", err);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-900 px-4 py-12 sm:px-6 lg:px-8">
      <div className="w-full max-w-md space-y-8 rounded-lg bg-gray-800 p-8 shadow-lg">
        <div>
          <h2 className="mt-6 text-center text-3xl font-bold tracking-tight text-white">
            Discodeit에 로그인
          </h2>
        </div>

        {(error || localError) && (
          <div className="rounded-md bg-red-500 bg-opacity-10 p-3 text-sm text-red-400">
            {error || localError}
          </div>
        )}

        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="-space-y-px rounded-md shadow-sm">
            <div>
              <label htmlFor="username" className="sr-only">
                사용자명
              </label>
              <input
                id="username"
                name="username"
                type="text"
                required
                className="input relative block w-full rounded-t-md border-0 py-2 px-3 text-white placeholder-gray-400 focus:z-10"
                placeholder="사용자명"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>
            <div>
              <label htmlFor="password" className="sr-only">
                비밀번호
              </label>
              <input
                id="password"
                name="password"
                type="password"
                required
                className="input relative block w-full rounded-b-md border-0 py-2 px-3 text-white placeholder-gray-400 focus:z-10"
                placeholder="비밀번호"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </div>

          <div>
            <button
              type="submit"
              disabled={loading}
              className="btn-primary btn group relative flex w-full justify-center"
            >
              {loading ? "로그인 중..." : "로그인"}
            </button>
          </div>

          <div className="text-center text-sm">
            <p className="text-gray-400">
              계정이 없으신가요?{" "}
              <button
                type="button"
                onClick={onGoRegister}
                className="font-medium text-indigo-400 hover:text-indigo-300"
              >
                회원가입
              </button>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
};

export default LoginForm;
