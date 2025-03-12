import styled from 'styled-components';

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
`;

const ModalContainer = styled.div`
  background: ${({ theme }) => theme.colors.background.primary};
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
`;

const ModalHeader = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 16px;
`;

const ErrorIcon = styled.div`
  color: ${({ theme }) => theme.colors.status.error};
  font-size: 24px;
  margin-right: 12px;
`;

const Title = styled.h3`
  color: ${({ theme }) => theme.colors.text.primary};
  margin: 0;
  font-size: 18px;
`;

const StatusCode = styled.div`
  background: ${({ theme }) => theme.colors.background.tertiary};
  color: ${({ theme }) => theme.colors.text.muted};
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 14px;
  margin-left: auto;
`;

const Message = styled.p`
  color: ${({ theme }) => theme.colors.text.secondary};
  margin-bottom: 20px;
  line-height: 1.5;
`;

const Button = styled.button`
  background: ${({ theme }) => theme.colors.brand.primary};
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  width: 100%;
  
  &:hover {
    background: ${({ theme }) => theme.colors.brand.hover};
  }
`;

interface ErrorModalProps {
  isOpen: boolean;
  onClose: () => void;
  error: any; // 실제 에러 타입에 맞게 수정 가능
}

function ErrorModal({ isOpen, onClose, error }: ErrorModalProps): JSX.Element | null {
  if (!isOpen) return null;
  
  const statusCode = error?.response?.status || '오류';
  const errorMessage = error?.response?.data || error?.message || '알 수 없는 오류가 발생했습니다.';
  
  return (
    <ModalOverlay onClick={onClose}>
      <ModalContainer onClick={e => e.stopPropagation()}>
        <ModalHeader>
          <ErrorIcon>⚠️</ErrorIcon>
          <Title>오류가 발생했습니다</Title>
          {statusCode && <StatusCode>{statusCode}</StatusCode>}
        </ModalHeader>
        <Message>{errorMessage}</Message>
        <Button onClick={onClose}>확인</Button>
      </ModalContainer>
    </ModalOverlay>
  );
}

export default ErrorModal; 