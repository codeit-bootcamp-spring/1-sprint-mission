import styled from 'styled-components'
import { theme } from '../../styles/theme'
import { StatusDot, AvatarContainer } from '../../styles/common'

export const StyledChatContainer = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  background: ${({ theme }) => theme.colors.background.primary};
`

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
  background: ${({ theme }) => theme.colors.background.primary};
`

export const EmptyContainer = styled(Container)`
  justify-content: center;
  align-items: center;
  flex: 1;
  padding: 0 20px;
`

export const EmptyContent = styled.div`
  text-align: center;
  max-width: 400px;
  padding: 20px;
  margin-bottom: 80px;
`

export const WelcomeIcon = styled.div`
  font-size: 48px;
  margin-bottom: 16px;
  animation: wave 2s infinite;
  transform-origin: 70% 70%;

  @keyframes wave {
    0% { transform: rotate(0deg); }
    10% { transform: rotate(14deg); }
    20% { transform: rotate(-8deg); }
    30% { transform: rotate(14deg); }
    40% { transform: rotate(-4deg); }
    50% { transform: rotate(10deg); }
    60% { transform: rotate(0deg); }
    100% { transform: rotate(0deg); }
  }
`

export const WelcomeTitle = styled.h2`
  color: ${({ theme }) => theme.colors.text.primary};
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 16px;
`

export const WelcomeText = styled.p`
  color: ${({ theme }) => theme.colors.text.muted};
  font-size: 16px;
  line-height: 1.6;
  word-break: keep-all;
`

export const StyledChatHeader = styled.div`
  height: 48px;
  padding: 0 16px;
  background: ${theme.colors.background.primary};
  border-bottom: 1px solid ${theme.colors.border.primary};
  display: flex;
  align-items: center;
`

export const HeaderLeft = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
  height: 100%;
`

export const HeaderPrivateInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 12px;
  height: 100%;
`

export const PrivateAvatarContainer = styled(AvatarContainer)`
  width: 24px;
  height: 24px;
`

export const PrivateAvatar = styled.img`
  width: 24px;
  height: 24px;
  border-radius: 50%;
`

export const GroupAvatarContainer = styled.div`
  position: relative;
  width: 40px;
  height: 24px;
  flex-shrink: 0;
`

export const HeaderStatusDot = styled(StatusDot)`
  border-color: ${theme.colors.background.primary};
  bottom: -3px;
  right: -3px;
`

export const ParticipantCount = styled.div`
  font-size: 12px;
  color: ${theme.colors.text.muted};
  line-height: 13px;
`

export const HeaderChannelName = styled.div`
  font-weight: bold;
  color: ${theme.colors.text.primary};
  line-height: 20px;
  font-size: 16px;
`

export const MessageListWrapper = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column-reverse;
  overflow-y: auto;
`

export const StyledMessageList = styled.div`
  padding: 16px;
  display: flex;
  flex-direction: column;
`

export const MessageItem = styled.div`
  margin-bottom: 16px;
  display: flex;
  align-items: flex-start;
`

export const AuthorAvatarContainer = styled(AvatarContainer)`
  margin-right: 16px;
  width: 40px;
  height: 40px;
`

export const AuthorAvatar = styled.img`
  width: 40px;
  height: 40px;
  border-radius: 50%;
`

export const MessageHeader = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 4px;
`

export const MessageAuthor = styled.span`
  font-weight: bold;
  color: ${theme.colors.text.primary};
  margin-right: 8px;
`

export const MessageTime = styled.span`
  font-size: 0.75rem;
  color: ${theme.colors.text.muted};
`

export const MessageContent = styled.div`
  color: ${theme.colors.text.secondary};
  margin-top: 4px;
`

export const StyledMessageInput = styled.form`
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px;
  background: ${({ theme }) => theme.colors.background.secondary};
`

export const Input = styled.textarea`
  flex: 1;
  padding: 12px;
  background: ${({ theme }) => theme.colors.background.tertiary};
  border: none;
  border-radius: 4px;
  color: ${({ theme }) => theme.colors.text.primary};
  font-size: 14px;
  resize: none;
  min-height: 44px;
  max-height: 144px;

  &:focus {
    outline: none;
  }

  &::placeholder {
    color: ${({ theme }) => theme.colors.text.muted};
  }
`

export const AttachButton = styled.button`
  background: none;
  border: none;
  color: ${({ theme }) => theme.colors.text.muted};
  font-size: 24px;
  cursor: pointer;
  padding: 4px 8px;
  display: flex;
  align-items: center;
  justify-content: center;

  &:hover {
    color: ${({ theme }) => theme.colors.text.primary};
  }
`

export const EmptyState = styled.div`
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: ${theme.colors.text.muted};
  font-size: 16px;
  font-weight: 500;
  padding: 20px;
  text-align: center;
`

export const AttachmentList = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 8px;
  width: 100%;
`

export const ImagePreview = styled.a`
  display: block;
  border-radius: 4px;
  overflow: hidden;
  max-width: 300px;
  
  img {
    width: 100%;
    height: auto;
    display: block;
  }
`

export const FileItem = styled.a`
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: ${({ theme }) => theme.colors.background.tertiary};
  border-radius: 8px;
  text-decoration: none;
  width: fit-content;

  &:hover {
    background: ${({ theme }) => theme.colors.background.hover};
  }
`

export const FileIcon = styled.div`
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  color: #0B93F6;
`

export const FileInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2px;
`

export const FileName = styled.span`
  font-size: 14px;
  color: #0B93F6;
  font-weight: 500;
`

export const FileSize = styled.span`
  font-size: 13px;
  color: ${({ theme }) => theme.colors.text.muted};
`

export const AttachmentPreviewList = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 8px 0;
`

export const AttachmentPreviewItem = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: ${({ theme }) => theme.colors.background.tertiary};
  border-radius: 4px;
  max-width: 300px;
`

export const ImagePreviewItem = styled(AttachmentPreviewItem)`
  padding: 0;
  overflow: hidden;
  width: 200px;
  height: 120px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`

export const PreviewFileIcon = styled.div`
  color: #0B93F6;
  font-size: 20px;
`

export const PreviewFileName = styled.div`
  font-size: 13px;
  color: ${({ theme }) => theme.colors.text.primary};
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`

export const RemoveButton = styled.button`
  position: absolute;
  top: -6px;
  right: -6px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: ${({ theme }) => theme.colors.background.secondary};
  border: none;
  color: ${({ theme }) => theme.colors.text.muted};
  font-size: 16px;
  line-height: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);

  &:hover {
    color: ${({ theme }) => theme.colors.text.primary};
  }
` 