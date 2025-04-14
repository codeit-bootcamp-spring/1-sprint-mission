import { create } from 'zustand';
import { getMessages, createMessage as apiCreateMessage } from '../api/message';
import useReadStatusStore from './readStatusStore';
import { MessageDto, MessageCreateRequest, Pageable } from '../types/api';

interface PollingIntervals {
  [channelId: string]: NodeJS.Timeout | boolean;
}

interface CursorPagination {
  nextCursor: string | null;
  pageSize: number;
  hasNext: boolean;
}

interface MessageStore {
  messages: MessageDto[];
  pollingIntervals: PollingIntervals;
  lastMessageId: string | null;
  pagination: CursorPagination;
  fetchMessages: (channelId: string, cursor: string | null, pageable?: Pageable) => Promise<boolean>;
  loadMoreMessages: (channelId: string) => Promise<void>;
  startPolling: (channelId: string) => void;
  stopPolling: (channelId: string) => void;
  createMessage: (messageData: MessageCreateRequest, attachments?: File[]) => Promise<MessageDto>;
}

const defaultPageable: Pageable = {
  size: 50,
  sort: ["createdAt,desc"]
};

const useMessageStore = create<MessageStore>((set, get) => ({
  messages: [],
  pollingIntervals: {},  // channelId를 key로 하는 polling interval map
  lastMessageId: null,  // 마지막 메시지 ID 저장
  pagination: {
    nextCursor: null,
    pageSize: 50,
    hasNext: false,
  },

  fetchMessages: async (channelId, cursor, pageable = defaultPageable) => {
    try {
      const response = await getMessages(channelId, cursor, pageable);
      
      const messageList = response.content;
      const lastMessage = messageList.length > 0 ? messageList[0] : null;
      const hasNewMessages = lastMessage?.id !== get().lastMessageId;
      
      set((state) => {
        const isPolling = !cursor;
        const isChannelChanged = channelId !== state.messages[0]?.channelId;
        const isFirstPolling = isPolling && (state.messages.length === 0 || isChannelChanged);
        let updatedMessages = [];
        let pagination = { ...state.pagination };
        
        if (isFirstPolling) {
          // 최초 로딩 시
          updatedMessages = messageList;
          pagination = {
            nextCursor: response.nextCursor,
            pageSize: response.size,
            hasNext: response.hasNext
          };
        } else if (isPolling) {
          // 폴링 업데이트 시 (새 메시지 추가)
          // ID 기반 중복 체크 추가
          const existingMessageIds = new Set(state.messages.map(msg => msg.id));
          const newMessages = messageList.filter(message => 
            !existingMessageIds.has(message.id) && 
            (state.messages.length === 0 || message.createdAt > state.messages[0].createdAt)
          );
          updatedMessages = [...newMessages, ...state.messages];
        } else {
          // 이전 메시지 로드 시 (무한 스크롤)
          // ID 기반 중복 체크 추가
          const existingMessageIds = new Set(state.messages.map(msg => msg.id));
          const loadedMessages = messageList.filter(message => !existingMessageIds.has(message.id));
          updatedMessages = [...state.messages, ...loadedMessages];
          pagination = {
            nextCursor: response.nextCursor,
            pageSize: response.size,
            hasNext: response.hasNext
          };
        }
          
        return {
          messages: updatedMessages,
          lastMessageId: lastMessage?.id || null,
          pagination
        };
      });

      return hasNewMessages;
    } catch (error) {
      console.error('메시지 목록 조회 실패:', error);
      return false;
    }
  },

  loadMoreMessages: async (channelId) => {
    const { pagination } = get();
    
    if (!pagination.hasNext) return;
    
    await get().fetchMessages(channelId, pagination.nextCursor, {
      ...defaultPageable
    });
  },

  startPolling: (channelId) => {
    const store = get();
    
    // 이전에 실행 중이던 같은 채널의 폴링이 있다면 정리
    if (store.pollingIntervals[channelId]) {
      const timeoutId = store.pollingIntervals[channelId];
      if (typeof timeoutId === 'number') {
        clearTimeout(timeoutId);
      }
    }

    let pollInterval = 300;
    const maxInterval = 3000;
    
    // 폴링 시작 시점에 해당 채널의 폴링 상태를 true로 설정
    set((state) => ({
      pollingIntervals: {
        ...state.pollingIntervals,
        [channelId]: true
      }
    }));
    
    const doPoll = async () => {
      // 현재 store의 최신 상태 가져오기
      const currentStore = get();
      
      // 해당 채널의 폴링이 이미 중지되었다면 더 이상 진행하지 않음
      if (!currentStore.pollingIntervals[channelId]) {
        return;
      }

      // 커서 없이 폴링 (최신 메시지만 가져오기)
      const hasNewMessages = await currentStore.fetchMessages(channelId, null, defaultPageable);
      
      if (hasNewMessages) {
        pollInterval = 300;
      } else {
        pollInterval = Math.min(pollInterval * 1.5, maxInterval);
      }

      // 다음 폴링 예약 전에 다시 한번 채널 폴링 상태 확인
      if (get().pollingIntervals[channelId]) {
        const timeoutId = setTimeout(doPoll, pollInterval);
        set((state) => ({
          pollingIntervals: {
            ...state.pollingIntervals,
            [channelId]: timeoutId
          }
        }));
      }
    };

    doPoll();
  },

  stopPolling: (channelId) => {
    const { pollingIntervals } = get();
    if (pollingIntervals[channelId]) {
      const timeoutId = pollingIntervals[channelId];
      if (typeof timeoutId === 'number') {
        clearTimeout(timeoutId);
      }
      set((state) => {
        const newPollingIntervals = { ...state.pollingIntervals };
        delete newPollingIntervals[channelId];
        return { pollingIntervals: newPollingIntervals };
      });
    }
  },

  createMessage: async (messageData, attachments) => {
    try {
      const newMessage = await apiCreateMessage(messageData, attachments);

      // 메시지 전송 성공 시 readStatus 업데이트
      const updateReadStatus = useReadStatusStore.getState().updateReadStatus;
      await updateReadStatus(messageData.channelId);

      set((state) => {
        // 중복 체크 추가
        const messageExists = state.messages.some(msg => msg.id === newMessage.id);
        if (messageExists) {
          return state; // 이미 존재하는 메시지면 상태 변경 없음
        }
        return {
          messages: [newMessage, ...state.messages], // 최신 메시지가 앞에 오도록 추가
          lastMessageId: newMessage.id // 마지막 메시지 ID 업데이트
        };
      });
      return newMessage;
    } catch (error) {
      console.error('메시지 생성 실패:', error);
      throw error;
    }
  }
}));

export default useMessageStore; 