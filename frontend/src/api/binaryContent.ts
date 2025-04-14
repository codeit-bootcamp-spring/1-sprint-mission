import { BinaryContentDto } from '../types/api';
import client, { getBaseUrl } from './client';

export const getBinaryContent = async (attachmentId: string): Promise<BinaryContentDto> => {
  const response = await client.get<BinaryContentDto>(`/binaryContents/${attachmentId}`);
  return response.data;
};

export const getBinaryContents = async (attachmentIds: string[]): Promise<BinaryContentDto[]> => {
  const response = await client.get<BinaryContentDto[]>('/binaryContents', {
    params: { binaryContentIds: attachmentIds }
  });
  return response.data;
};

interface DownloadResult {
  blob: Blob;
}

export const downloadBinaryContent = async (attachmentId: string): Promise<DownloadResult> => {
  const response = await client.get(`/binaryContents/${attachmentId}/download`, {
    responseType: 'blob'
  });
  
  return {
    blob: response.data,
  };
}; 

export const genBinaryContentUrl = (binaryContentId: string): string => {
  const baseUrl = getBaseUrl();
  return `${baseUrl}/binaryContents/${binaryContentId}/download`;
}