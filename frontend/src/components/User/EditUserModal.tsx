import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import useUserStore from '@/stores/userStore';
import defaultProfile from '@/assets/default_profile.png';
import { updateUser } from '../../api/user';
import useBinaryContentStore from '@/stores/binaryContentStore';
import { UserDto } from '@/types/api';

interface EditUserModalProps {
  isOpen: boolean;
  onClose: () => void;
  user: UserDto;
}

function EditUserModal({ isOpen, onClose, user }: EditUserModalProps): JSX.Element | null {
  const [username, setUsername] = useState(user.username);
  const [email, setEmail] = useState(user.email);
  const [password, setPassword] = useState('');
  const [profileImage, setProfileImage] = useState<File | null>(null);
  const [error, setError] = useState('');
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);
  const {binaryContents, fetchBinaryContent} = useBinaryContentStore();
  const logoutUser = useUserStore((state) => state.logout);

  useEffect(() => {
    if (user.profile?.id && !binaryContents[user.profile.id]) {
      fetchBinaryContent(user.profile.id);
    }
  }, [user.profile, binaryContents, fetchBinaryContent]);

  const handleClose = () => {
    setUsername(user.username);
    setEmail(user.email);
    setPassword('');
    setProfileImage(null);
    setPreviewUrl(null);
    setError('');
    onClose();
  };

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setProfileImage(file);
      // 이미지 미리보기 생성
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreviewUrl(reader.result as string);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');

    try {
      const formData = new FormData();
      
      // 변경된 필드만 포함하는 업데이트 요청 객체 생성
      const updateRequest: Record<string, string> = {};
      if (username !== user.username) {
        updateRequest.newUsername = username;
      }
      if (email !== user.email) {
        updateRequest.newEmail = email;
      }
      if (password) {
        updateRequest.newPassword = password;
      }

      // 변경된 필드가 있거나 프로필 이미지가 변경된 경우에만 요청 전송
      if (Object.keys(updateRequest).length > 0 || profileImage) {
        formData.append('userUpdateRequest', new Blob([JSON.stringify(updateRequest)], { 
          type: 'application/json' 
        }));

        if (profileImage) {
          formData.append('profile', profileImage);
        }

        await updateUser(user.id, formData);
        onClose();
      } else {
        onClose(); // 변경사항이 없으면 그냥 모달 닫기
      }
    } catch (error) {
      setError('사용자 정보 수정에 실패했습니다.');
    }
  };

  if (!isOpen) return null;

  return (
    <StyledModal>
      <ModalContent>
        <h2>프로필 수정</h2>
        <form onSubmit={handleSubmit}>
          <FormField>
            <Label>프로필 이미지</Label>
            <ImageContainer>
              <ProfileImage 
                src={previewUrl || (user.profile?.id ? binaryContents[user.profile.id]?.url : undefined) || defaultProfile} 
                alt="profile" 
              />
              <ImageInput
                type="file"
                accept="image/*"
                onChange={handleImageChange}
                id="profile-image"
              />
              <ImageLabel htmlFor="profile-image">
                이미지 변경
              </ImageLabel>
            </ImageContainer>
          </FormField>
          <FormField>
            <Label>사용자명 <Required>*</Required></Label>
            <Input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </FormField>
          <FormField>
            <Label>이메일 <Required>*</Required></Label>
            <Input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </FormField>
          <FormField>
            <Label>새 비밀번호</Label>
            <Input
              type="password"
              placeholder="변경하지 않으려면 비워두세요"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </FormField>
          {error && <ErrorMessage>{error}</ErrorMessage>}
          <ButtonGroup>
            <Button type="button" onClick={handleClose} $secondary>취소</Button>
            <Button type="submit">저장</Button>
          </ButtonGroup>
        </form>
        <LogoutButton onClick={logoutUser}>로그아웃</LogoutButton>
      </ModalContent>
    </StyledModal>
  );
}

const StyledModal = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
`;

const ModalContent = styled.div`
  background: ${({ theme }) => theme.colors.background.secondary};
  padding: 32px;
  border-radius: 5px;
  width: 100%;
  max-width: 480px;

  h2 {
    color: ${({ theme }) => theme.colors.text.primary};
    margin-bottom: 24px;
    text-align: center;
    font-size: 24px;
  }
`;

const Input = styled.input`
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: none;
  border-radius: 4px;
  background: ${({ theme }) => theme.colors.background.input};
  color: ${({ theme }) => theme.colors.text.primary};

  &::placeholder {
    color: ${({ theme }) => theme.colors.text.muted};
  }

  &:focus {
    outline: none;
    box-shadow: 0 0 0 2px ${({ theme }) => theme.colors.brand.primary};
  }
`;

interface ButtonProps {
  $secondary?: boolean;
}

const Button = styled.button<ButtonProps>`
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 4px;
  background: ${({ $secondary, theme }) => 
    $secondary ? 'transparent' : theme.colors.brand.primary};
  color: ${({ theme }) => theme.colors.text.primary};
  cursor: pointer;
  font-weight: 500;
  
  &:hover {
    background: ${({ $secondary, theme }) => 
      $secondary ? theme.colors.background.hover : theme.colors.brand.hover};
  }
`;

const ErrorMessage = styled.div`
  color: ${({ theme }) => theme.colors.status.error};
  font-size: 14px;
  margin-bottom: 10px;
`;

const ImageContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
`;

const ProfileImage = styled.img`
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-bottom: 10px;
  object-fit: cover;
`;

const ImageInput = styled.input`
  display: none;
`;

const ImageLabel = styled.label`
  color: ${({ theme }) => theme.colors.brand.primary};
  cursor: pointer;
  font-size: 14px;
  
  &:hover {
    text-decoration: underline;
  }
`;

const ButtonGroup = styled.div`
  display: flex;
  gap: 10px;
  margin-top: 20px;
`;

const LogoutButton = styled.button`
  width: 100%;
  padding: 10px;
  margin-top: 16px;
  border: none;
  border-radius: 4px;
  background: transparent;
  color: ${({ theme }) => theme.colors.status.error};
  cursor: pointer;
  font-weight: 500;
  
  &:hover {
    background: ${({ theme }) => theme.colors.status.error}20;
  }
`;

const FormField = styled.div`
  margin-bottom: 20px;
`;

const Label = styled.label`
  display: block;
  color: ${({ theme }) => theme.colors.text.muted};
  font-size: 12px;
  font-weight: 700;
  margin-bottom: 8px;
`;

const Required = styled.span`
  color: ${({ theme }) => theme.colors.status.error};
`;

export default EditUserModal; 