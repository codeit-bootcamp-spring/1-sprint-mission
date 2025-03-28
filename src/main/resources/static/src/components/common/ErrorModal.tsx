import styled from 'styled-components';
import { ErrorResponse } from '@/api/client';
import { formatDate } from '../../utils/dateUtils';

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
  width: 500px;
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

const StatusBadge = styled.div`
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
  font-weight: 500;
`;

const ErrorDetailsContainer = styled.div`
  margin-bottom: 20px;
  background: ${({ theme }) => theme.colors.background.secondary};
  border-radius: 6px;
  padding: 12px;
`;

const ErrorDetailItem = styled.div`
  display: flex;
  margin-bottom: 8px;
  font-size: 14px;
`;

const ErrorDetailLabel = styled.span`
  color: ${({ theme }) => theme.colors.text.muted};
  min-width: 100px;
`;

const ErrorDetailValue = styled.span`
  color: ${({ theme }) => theme.colors.text.secondary};
  word-break: break-word;
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
  error: any; // API 에러 또는 일반 에러 객체
}

function ErrorModal({ isOpen, onClose, error }: ErrorModalProps): JSX.Element | null {
  if (!isOpen) return null;
  
  // 서버에서 정의한 ErrorResponse 타입으로 처리
  const errorResponse: ErrorResponse | undefined = error?.response?.data;
  
  // 에러 정보 추출
  const status = errorResponse?.status || error?.response?.status || '오류';
  const code = errorResponse?.code || '';
  const message = errorResponse?.message || error?.message || '알 수 없는 오류가 발생했습니다.';
  const timestamp = errorResponse?.timestamp ? new Date(errorResponse.timestamp) : new Date();
  const formattedTimestamp = formatDate(timestamp);
  const exceptionType = errorResponse?.exceptionType || '';
  const details = errorResponse?.details || {};
  const requestId = errorResponse?.requestId || '';
  
  return (
    <ModalOverlay onClick={onClose}>
      <ModalContainer onClick={e => e.stopPropagation()}>
        <ModalHeader>
          <ErrorIcon>⚠️</ErrorIcon>
          <Title>오류가 발생했습니다</Title>
          {status && <StatusBadge>{status}{code ? ` (${code})` : ''}</StatusBadge>}
        </ModalHeader>
        
        <Message>{message}</Message>
        
        <ErrorDetailsContainer>
          <ErrorDetailItem>
            <ErrorDetailLabel>시간:</ErrorDetailLabel>
            <ErrorDetailValue>{formattedTimestamp}</ErrorDetailValue>
          </ErrorDetailItem>
          
          {requestId && (
            <ErrorDetailItem>
              <ErrorDetailLabel>요청 ID:</ErrorDetailLabel>
              <ErrorDetailValue>{requestId}</ErrorDetailValue>
            </ErrorDetailItem>
          )}
          
          {code && (
            <ErrorDetailItem>
              <ErrorDetailLabel>에러 코드:</ErrorDetailLabel>
              <ErrorDetailValue>{code}</ErrorDetailValue>
            </ErrorDetailItem>
          )}
          
          {exceptionType && (
            <ErrorDetailItem>
              <ErrorDetailLabel>예외 유형:</ErrorDetailLabel>
              <ErrorDetailValue>{exceptionType}</ErrorDetailValue>
            </ErrorDetailItem>
          )}
          
          {Object.keys(details).length > 0 && (
            <ErrorDetailItem>
              <ErrorDetailLabel>상세 정보:</ErrorDetailLabel>
              <ErrorDetailValue>
                {Object.entries(details).map(([key, value]) => (
                  <div key={key}>{key}: {String(value)}</div>
                ))}
              </ErrorDetailValue>
            </ErrorDetailItem>
          )}
        </ErrorDetailsContainer>
        
        <Button onClick={onClose}>확인</Button>
      </ModalContainer>
    </ModalOverlay>
  );
}

export default ErrorModal; 