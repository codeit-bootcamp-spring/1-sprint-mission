import client from './client';
import { 
  ChannelDto, 
  PublicChannelCreateRequest, 
  PrivateChannelCreateRequest,
  PublicChannelUpdateRequest
} from '../types/api';

export const getChannels = async (userId: string): Promise<ChannelDto[]> => {
  const response = await client.get<ChannelDto[]>(`/channels?userId=${userId}`);
  return response.data;
};

export const createPublicChannel = async (channelData: PublicChannelCreateRequest): Promise<ChannelDto> => {
  const response = await client.post<ChannelDto>('/channels/public', channelData);
  return response.data;
};

export const createPrivateChannel = async (participantIds: string[]): Promise<ChannelDto> => {
  const request: PrivateChannelCreateRequest = { participantIds };
  const response = await client.post<ChannelDto>('/channels/private', request);
  return response.data;
};

export const updateChannel = async (channelId: string, updateData: PublicChannelUpdateRequest): Promise<ChannelDto> => {
  const response = await client.patch<ChannelDto>(`/channels/${channelId}`, updateData);
  return response.data;
};

export const deleteChannel = async (channelId: string): Promise<void> => {
  await client.delete(`/channels/${channelId}`);
}; 