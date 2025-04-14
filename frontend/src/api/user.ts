import client from './client';
import { UserDto, UserStatusDto } from '../types/api';

export const updateUser = async (userId: string, formData: FormData): Promise<UserDto> => {
  const response = await client.patch<UserDto>(`/users/${userId}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
  return response.data;
};

export const getUsers = async (): Promise<UserDto[]> => {
  const response = await client.get<UserDto[]>('/users');
  return response.data;
};

export const updateUserStatus = async (userId: string): Promise<UserStatusDto> => {
  const response = await client.patch<UserStatusDto>(`/users/${userId}/userStatus`, {
    newLastActiveAt: new Date().toISOString()
  });
  return response.data;
};