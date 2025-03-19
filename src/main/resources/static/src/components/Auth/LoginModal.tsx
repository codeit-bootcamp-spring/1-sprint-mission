import React, { useState } from 'react'
import { login } from '../../api/auth'
import useUserListStore from '../../stores/userListStore'
import useUserStore from '../../stores/userStore'
import SignUpModal from './SignUpModal'
import {
  Button,
  ErrorMessage,
  Input,
  ModalContent,
  SignUpLink,
  SignUpText,
  StyledLoginModal
} from './styles'

interface LoginModalProps {
  isOpen: boolean;
  onClose: () => void;
}

const LoginModal: React.FC<LoginModalProps> = ({ isOpen, onClose }) => {
  const [usernameOrEmail, setUsernameOrEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isSignUpOpen, setIsSignUpOpen] = useState(false);
  const setCurrentUser = useUserStore((state) => state.setCurrentUser);
  const { fetchUsers } = useUserListStore();

  const handleLogin = async () => {
    try {
      const userData = await login(usernameOrEmail, password);
      await fetchUsers();
      setCurrentUser(userData);
      setError('');
      onClose();
    } catch (error: any) {
      console.error('로그인 에러:', error);
      if (error.response?.status === 401) {
        setError('아이디 또는 비밀번호가 올바르지 않습니다.');
      } else {
        setError('로그인에 실패했습니다.');
      }
    }
  };

  if (!isOpen) return null;

  return (
    <>
      <StyledLoginModal>
        <ModalContent>
          <h2>돌아오신 것을 환영해요!</h2>
          <form onSubmit={(e) => {
            e.preventDefault();
            handleLogin();
          }}>
            <Input
              type="text"
              placeholder="사용자 이름"
              value={usernameOrEmail}
              onChange={(e) => setUsernameOrEmail(e.target.value)}
            />
            <Input
              type="password"
              placeholder="비밀번호"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            {error && <ErrorMessage>{error}</ErrorMessage>}
            <Button type="submit">로그인</Button>
          </form>
          <SignUpText>
            계정이 필요한가요? <SignUpLink onClick={() => setIsSignUpOpen(true)}>가입하기</SignUpLink>
          </SignUpText>
        </ModalContent>
      </StyledLoginModal>
      <SignUpModal 
        isOpen={isSignUpOpen}
        onClose={() => setIsSignUpOpen(false)}
      />
    </>
  );
};

export default LoginModal; 