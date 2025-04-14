import styled from 'styled-components'
import { theme } from '../../styles/theme'

export const StyledLoginModal = styled.div`
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
`

export const ModalContent = styled.div`
  background: ${theme.colors.background.primary};
  padding: 32px;
  border-radius: 8px;
  width: 440px;

  h2 {
    color: ${theme.colors.text.primary};
    margin-bottom: 24px;
    font-size: 24px;
    font-weight: bold;
  }

  form {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }
`

export const Input = styled.input`
  width: 100%;
  padding: 10px;
  border-radius: 4px;
  background: ${theme.colors.background.input};
  border: none;
  color: ${theme.colors.text.primary};
  font-size: 16px;

  &::placeholder {
    color: ${theme.colors.text.muted};
  }

  &:focus {
    outline: none;
  }
`

export const Button = styled.button`
  width: 100%;
  padding: 12px;
  border-radius: 4px;
  background: ${theme.colors.brand.primary};
  color: white;
  font-size: 16px;
  font-weight: 500;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background: ${theme.colors.brand.hover};
  }
`

export const ErrorMessage = styled.div`
  color: ${theme.colors.status.error};
  font-size: 14px;
  text-align: center;
`

export const SignUpText = styled.p`
  text-align: center;
  margin-top: 16px;
  color: ${({ theme }) => theme.colors.text.muted};
  font-size: 14px;
`;

export const SignUpLink = styled.span`
  color: ${({ theme }) => theme.colors.brand.primary};
  cursor: pointer;
  
  &:hover {
    text-decoration: underline;
  }
`;

// SignUpModal에서 가져온 스타일 컴포넌트
export const FormField = styled.div`
  margin-bottom: 20px;
`;

export const Label = styled.label`
  display: block;
  color: ${({ theme }) => theme.colors.text.muted};
  font-size: 12px;
  font-weight: 700;
  margin-bottom: 8px;
`;

export const Required = styled.span`
  color: ${({ theme }) => theme.colors.status.error};
`;

export const ImageContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 10px 0;
`;

export const ProfileImage = styled.img`
  width: 80px;
  height: 80px;
  border-radius: 50%;
  margin-bottom: 10px;
  object-fit: cover;
`;

export const ImageInput = styled.input`
  display: none;
`;

export const ImageLabel = styled.label`
  color: ${({ theme }) => theme.colors.brand.primary};
  cursor: pointer;
  font-size: 14px;
  
  &:hover {
    text-decoration: underline;
  }
`;

export const Link = styled.span`
  color: ${({ theme }) => theme.colors.brand.primary};
  cursor: pointer;
  
  &:hover {
    text-decoration: underline;
  }
`;

export const LoginLink = styled(Link)`
  display: block;
  text-align: center;
  margin-top: 16px;
`; 