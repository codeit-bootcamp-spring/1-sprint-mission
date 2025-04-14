import client from './client';
import { MessageDto, MessageCreateRequest, MessageUpdateRequest, Pageable, PageResponse } from '../types/api';

export const getMessages = async (channelId: string, cursor: string | null, pageable: Pageable): Promise<PageResponse<MessageDto>> => {
  const response = await client.get<PageResponse<MessageDto>>(`/messages`, {
    params: {
      channelId,
      cursor,
      size: pageable.size,
      sort: pageable.sort?.join(',')
    }
  });
  return response.data;
};

export const createMessage = async (messageData: MessageCreateRequest, attachments?: File[]): Promise<MessageDto> => {
  const formData = new FormData();
  
  // JSON 데이터 추가
  const messageRequest = {
    content: messageData.content,
    channelId: messageData.channelId,
    authorId: messageData.authorId
  };
  
  formData.append('messageCreateRequest', new Blob([JSON.stringify(messageRequest)], { 
    type: 'application/json' 
  }));
  
  // 첨부 파일 추가
  if (attachments && attachments.length > 0) {
    attachments.forEach(file => {
      formData.append('attachments', file);
    });
  }
  
  const response = await client.post<MessageDto>('/messages', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
  return response.data;
};

export const updateMessage = async (messageId: string, updateData: MessageUpdateRequest): Promise<MessageDto> => {
  const response = await client.patch<MessageDto>(`/messages/${messageId}`, updateData);
  return response.data;
};

export const deleteMessage = async (messageId: string): Promise<void> => {
  await client.delete(`/messages/${messageId}`);
}; 