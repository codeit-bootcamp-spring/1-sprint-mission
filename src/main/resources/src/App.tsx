import { ChannelDto } from '@/types/api';
import { useEffect, useState } from 'react';
import styled, { ThemeProvider } from 'styled-components';
import LoginModal from './components/Auth/LoginModal';
import ChannelList from './components/Channel/ChannelList';
import ChatContainer from './components/Chat/ChatContainer';
import ErrorModal from './components/common/ErrorModal';
import MemberList from './components/Member/MemberList';
import useUserListStore from './stores/userListStore';
import useUserStore from './stores/userStore';
import { theme } from './styles/theme';
import { eventEmitter } from './utils/eventEmitter';

function App(): JSX.Element {
  const currentUserId = useUserStore((state) => state.currentUserId);
  const logout = useUserStore((state) => state.logout);
  const users = useUserListStore((state) => state.users);
  const { fetchUsers, updateUserStatus } = useUserListStore();
  const [activeChannel, setActiveChannel] = useState<ChannelDto | null>(null);
    
  // 에러 상태 관리
  const [error, setError] = useState<any>(null);
  const [isErrorModalOpen, setIsErrorModalOpen] = useState(false);
  
  // 로딩 상태 추가
  const [isLoading, setIsLoading] = useState(true);

  const currentUser = currentUserId 
    ? users.find(user => user.id === currentUserId) 
    : null;

  // 초기 로딩 처리
  useEffect(() => {
    const initializeApp = async () => {
      try {
        // 사용자가 로그인되어 있으면 사용자 상태 업데이트 시도
        if (currentUserId) {
          try {
            // updateUserStatus가 성공하면 유효한 사용자로 간주
            await updateUserStatus(currentUserId);
            // 사용자 목록 가져오기
            await fetchUsers();
          } catch (error) {
            // 상태 업데이트 실패 시 로그아웃 처리 (사용자가 존재하지 않는 경우)
            console.warn('사용자 상태 업데이트 실패. 로그아웃합니다.', error);
            logout();
          }
        }
      } catch (error) {
        console.error('초기화 오류:', error);
      } finally {
        // 초기화 완료 후 로딩 상태 해제
        setIsLoading(false);
      }
    };

    initializeApp();
  }, [currentUserId, updateUserStatus, fetchUsers, logout]);

  // API 에러 이벤트 리스너
  useEffect(() => {
    const errorHandler = (error: any) => {
      setError(error);
      setIsErrorModalOpen(true);
    };
    
    const authErrorHandler = () => {
      logout();
    };
    
    const unsubscribeError = eventEmitter.on('api-error', errorHandler);
    const unsubscribeAuth = eventEmitter.on('auth-error', authErrorHandler);
    
    return () => {
      unsubscribeError('api-error', errorHandler);
      unsubscribeAuth('auth-error', authErrorHandler);
    };
  }, [logout]);

  // 사용자 상태 업데이트 폴링
  useEffect(() => {
    let intervalId: NodeJS.Timeout;

    if (currentUserId) {
      // 현재 사용자의 상태를 30초마다 업데이트
      updateUserStatus(currentUserId);
      intervalId = setInterval(() => {
        updateUserStatus(currentUserId);
      }, 30000);

      // 사용자 목록을 1분마다 새로고침
      const fetchInterval = setInterval(() => {
        fetchUsers();
      }, 60000);

      return () => {
        clearInterval(intervalId);
        clearInterval(fetchInterval);
      };
    }
    
    return undefined;
  }, [currentUserId, fetchUsers, updateUserStatus]);

  const closeErrorModal = () => {
    setIsErrorModalOpen(false);
    setError(null);
  };

  // 로딩 중일 때 로딩 화면 표시
  if (isLoading) {
    return (
      <ThemeProvider theme={theme}>
        <LoadingContainer>
          <LoadingSpinner />
        </LoadingContainer>
      </ThemeProvider>
    );
  }

  return (
    <ThemeProvider theme={theme}>
      {currentUser ? (
        <Layout>
          <ChannelList 
            currentUser={currentUser}
            activeChannel={activeChannel}
            onChannelSelect={setActiveChannel}
          />
          <ChatContainer channel={activeChannel} />
          <MemberList />
        </Layout>
      ) : (
        <LoginModal isOpen={true} onClose={() => {}} />
      )}
      
      <ErrorModal 
        isOpen={isErrorModalOpen}
        onClose={closeErrorModal}
        error={error}
      />
    </ThemeProvider>
  );
}

const Layout = styled.div`
  display: flex;
  height: 100vh;
  width: 100vw;
  position: relative;
`;

const LoadingContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background-color: ${({ theme }) => theme.colors.background.primary};
`;

const LoadingSpinner = styled.div`
  width: 40px;
  height: 40px;
  border: 4px solid ${({ theme }) => theme.colors.background.tertiary};
  border-top: 4px solid ${({ theme }) => theme.colors.brand.primary};
  border-radius: 50%;
  animation: spin 1s linear infinite;
  
  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
`;

export default App; 