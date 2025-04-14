import axios, { AxiosInstance, AxiosError, AxiosResponse } from 'axios';
import config from '@/config';
import { eventEmitter } from '../utils/eventEmitter';

// 서버 에러 응답 타입 정의
export interface ErrorResponse {
  timestamp: string;
  code: string;
  message: string;
  details: Record<string, any>;
  exceptionType: string;
  status: number;
  requestId?: string; // 요청 고유 ID
}

const client: AxiosInstance = axios.create({
  baseURL: config.apiBaseUrl,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 응답 인터셉터 추가
client.interceptors.response.use(
  (response: AxiosResponse) => {
    return response;
  },
  (error: AxiosError) => {
    // 서버 에러 응답을 ErrorResponse 타입으로 매핑
    const errorResponse = error.response?.data as ErrorResponse | undefined;
    
    // 에러 객체에 정제된 데이터 첨부
    if (errorResponse) {
      // 응답 헤더에서 요청 ID 추출하여 에러 응답에 추가
      const requestId = error.response?.headers?.['discodeit-request-id'];
      if (requestId) {
        errorResponse.requestId = requestId;
      }
      
      error.response!.data = errorResponse;
    }
    
    // 에러 이벤트 발생
    eventEmitter.emit('api-error', error);
    
    // 401 에러 처리 (인증 실패)
    if (error.response && error.response.status === 401) {
      eventEmitter.emit('auth-error');
    }
    
    return Promise.reject(error);
  }
);

// baseURL을 외부에서 참조할 수 있는 함수 추가
export const getBaseUrl = (): string => {
  return client.defaults.baseURL as string;
};

export default client; 