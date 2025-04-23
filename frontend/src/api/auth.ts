import client from './client';
import { UserDto, LoginRequest } from '../types/api';

export const login = async (username: string, password: string): Promise<UserDto> => {
  const loginRequest: LoginRequest = {
    username,
    password,
  };
  const response = await client.post<UserDto>('/auth/login', loginRequest);
  return response.data;
};

export const signup = async (formData: FormData): Promise<UserDto> => {
  const response = await client.post<UserDto>('/users', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
  return response.data;
}; 