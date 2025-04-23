import React, { useState } from 'react';
import { 
  StyledLoginModal as StyledModal, 
  ModalContent, 
  Input, 
  Button, 
  ErrorMessage,
  FormField,
  Label,
  Required,
  ImageContainer,
  ProfileImage,
  ImageInput,
  ImageLabel,
  LoginLink
} from './styles';
import { signup } from '../../api/auth'
import useUserStore from '@/stores/userStore';
import defaultProfile from '@/assets/default_profile.png';

interface SignUpModalProps {
  isOpen: boolean;
  onClose: () => void;
}

const SignUpModal: React.FC<SignUpModalProps> = ({ isOpen, onClose }) => {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [profileImage, setProfileImage] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);
  const [error, setError] = useState('');
  const setCurrentUser = useUserStore((state) => state.setCurrentUser);

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
      formData.append('userCreateRequest', new Blob([JSON.stringify({
        email,
        username,
        password
      })], { type: 'application/json' }));

      if (profileImage) {
        formData.append('profile', profileImage);
      }

      const userData = await signup(formData);
      setCurrentUser(userData);
      onClose();
    } catch (error) {
      setError('회원가입에 실패했습니다.');
    }
  };

  if (!isOpen) return null;

  return (
    <StyledModal>
      <ModalContent>
        <h2>계정 만들기</h2>
        <form onSubmit={handleSubmit}>
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
            <Label>사용자명 <Required>*</Required></Label>
            <Input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </FormField>
          <FormField>
            <Label>비밀번호 <Required>*</Required></Label>
            <Input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </FormField>
          <FormField>
            <Label>프로필 이미지</Label>
            <ImageContainer>
              <ProfileImage 
                src={previewUrl || defaultProfile} 
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
          {error && <ErrorMessage>{error}</ErrorMessage>}
          <Button type="submit">계속하기</Button>
          <LoginLink onClick={onClose}>이미 계정이 있으신가요?</LoginLink>
        </form>
      </ModalContent>
    </StyledModal>
  );
}

export default SignUpModal; 