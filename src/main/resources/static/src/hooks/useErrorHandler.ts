import { useState, useCallback } from 'react';
import axios, { AxiosError } from 'axios';

interface ErrorState {
  message: string;
  isVisible: boolean;
}

const useErrorHandler = () => {
  const [error, setError] = useState<ErrorState>({ message: '', isVisible: false });

  const handleError = useCallback((err: AxiosError | Error | unknown) => {
    let errorMessage = '알 수 없는 오류가 발생했습니다.';
    
    if (err instanceof Error) {
      errorMessage = err.message;
    }
    
    // Axios 에러 처리
    if (axios.isAxiosError(err)) {
      if (err.response) {
        // 서버에서 응답이 왔지만 에러 상태 코드인 경우
        errorMessage = err.response.data?.message || `Error ${err.response.status}: ${err.response.statusText}`;
      } else if (err.request) {
        // 요청은 보냈지만 응답이 없는 경우
        errorMessage = '서버에 연결할 수 없습니다. 네트워크 연결을 확인해주세요.';
      }
    }
    
    setError({ message: errorMessage, isVisible: true });
    
    // 3초 후 에러 메시지 숨기기
    setTimeout(() => {
      setError(prev => ({ ...prev, isVisible: false }));
    }, 3000);
  }, []);

  const clearError = useCallback(() => {
    setError({ message: '', isVisible: false });
  }, []);

  return { error, handleError, clearError };
};

export default useErrorHandler; 