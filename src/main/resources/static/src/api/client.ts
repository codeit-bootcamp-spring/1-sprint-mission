import axios, { AxiosInstance, AxiosError, AxiosResponse } from 'axios';
import config from '@/config';
import { eventEmitter } from '../utils/eventEmitter';

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