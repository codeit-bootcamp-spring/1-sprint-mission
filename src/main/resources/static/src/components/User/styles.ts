import styled from 'styled-components'
import { AvatarContainer, Avatar } from '../../styles/common'

export const StyledUserPanel = styled.div`
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.5rem 0.75rem;
  background-color: ${({ theme }) => theme.colors.background.tertiary};
  width: 100%;
  height: 52px;
`

export const UserAvatarContainer = styled(AvatarContainer)``

export const UserAvatar = styled(Avatar)``

export const UserInfo = styled.div`
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
`

export const UserName = styled.div`
  font-weight: 500;
  color: ${({ theme }) => theme.colors.text.primary};
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 0.875rem;
  line-height: 1.2;
`

export const UserStatus = styled.div`
  font-size: 0.75rem;
  color: ${({ theme }) => theme.colors.text.secondary};
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.2;
`

export const UserControls = styled.div`
  display: flex;
  align-items: center;
  flex-shrink: 0;
`

export const IconButton = styled.button`
  background: none;
  border: none;
  padding: 0.25rem;
  cursor: pointer;
  color: ${({ theme }) => theme.colors.text.secondary};
  font-size: 18px;
  
  &:hover {
    color: ${({ theme }) => theme.colors.text.primary};
  }
` 