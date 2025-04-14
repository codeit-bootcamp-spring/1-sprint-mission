// User 관련 타입
export interface UserDto {
  id: string; // UUID
  username: string;
  email: string;
  profile?: BinaryContentDto;
  online: boolean;
}

export interface UserCreateRequest {
  username: string;
  email: string;
  password: string;
}

export interface UserUpdateRequest {
  newUsername?: string;
  newEmail?: string;
  newPassword?: string;
}

export interface UserStatusUpdateRequest {
  newLastActiveAt: string;
}

export interface UserStatusDto {
  id: string; // UUID
  userId: string; // UUID
  lastActiveAt: string;
}

// Channel 관련 타입
export interface ChannelDto {
  id: string; // UUID
  type: 'PUBLIC' | 'PRIVATE';
  name: string;
  description: string;
  participants: UserDto[];
  lastMessageAt: string;
}

export interface PublicChannelCreateRequest {
  name: string;
  description: string;
}

export interface PrivateChannelCreateRequest {
  participantIds: string[]; // UUID 배열
}

export interface PublicChannelUpdateRequest {
  newName?: string;
  newDescription?: string;
}

// Message 관련 타입
export interface MessageDto {
  id: string; // UUID
  createdAt: string;
  updatedAt: string;
  content: string;
  channelId: string; // UUID
  author: UserDto;
  attachments: BinaryContentDto[];
}

export interface MessageCreateRequest {
  content: string;
  channelId: string; // UUID
  authorId: string; // UUID
}

export interface MessageUpdateRequest {
  newContent: string;
}

// ReadStatus 관련 타입
export interface ReadStatusDto {
  id: string; // UUID
  userId: string; // UUID
  channelId: string; // UUID
  lastReadAt: string;
}

export interface ReadStatusCreateRequest {
  userId: string; // UUID
  channelId: string; // UUID
  lastReadAt: string;
}

export interface ReadStatusUpdateRequest {
  newLastReadAt: string;
}

// BinaryContent 관련 타입
export interface BinaryContentDto {
  id: string; // UUID
  fileName: string;
  size: number;
  contentType: string;
}

// Auth 관련 타입
export interface LoginRequest {
  username: string;
  password: string;
}

// 페이징 관련 타입
export interface Pageable {
  size: number;
  sort?: string[];
}

export interface PageResponse<T> {
  content: T[];
  nextCursor: string | null;
  size: number;
  hasNext: boolean;
  totalElements: number;
} 