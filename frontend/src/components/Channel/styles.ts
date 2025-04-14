import styled from 'styled-components'
import { theme } from '@/styles/theme'
import { StatusDot, AvatarContainer } from '@/styles/common'

interface StyledChannelItemProps {
  $hasUnread?: boolean;
  $isActive?: boolean;
}

interface PrivateChannelNameProps {
  $isActive?: boolean;
  $hasUnread?: boolean;
}

interface FoldIconProps {
  $folded?: boolean;
}

interface ChannelSectionContentProps {
  $folded?: boolean;
}

interface PrivateChannelWrapperProps {
  hasSubtext?: boolean;
}

export const StyledChannelList = styled.div`
  width: 240px;
  background: ${theme.colors.background.secondary};
  border-right: 1px solid ${theme.colors.border.primary};
  display: flex;
  flex-direction: column;
`

export const ChannelScroll = styled.div`
  flex: 1;
  overflow-y: auto;
`

export const StyledChannelHeader = styled.div`
  padding: 16px;
  font-size: 16px;
  font-weight: bold;
  color: ${theme.colors.text.primary};
`

export const StyledChannelItem = styled.div<StyledChannelItemProps>`
  height: 34px;
  padding: 0 8px;
  margin: 1px 8px;
  display: flex;
  align-items: center;
  gap: 6px;
  color: ${props => props.$hasUnread ? props.theme.colors.text.primary : props.theme.colors.text.muted};
  font-weight: ${props => props.$hasUnread ? '600' : 'normal'};
  cursor: pointer;
  background: ${props => props.$isActive ? props.theme.colors.background.hover : 'transparent'};
  border-radius: 4px;
  
  &:hover {
    background: ${props => props.theme.colors.background.hover};
    color: ${props => props.theme.colors.text.primary};
  }
`

export const ChannelSection = styled.div`
  margin-bottom: 8px;
`

export const ChannelSectionHeader = styled.div`
  padding: 8px 16px;
  display: flex;
  align-items: center;
  color: ${theme.colors.text.muted};
  text-transform: uppercase;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  user-select: none;

  & > span:nth-child(2) {
    flex: 1;
    margin-right: auto;
  }

  &:hover {
    color: ${theme.colors.text.primary};
  }
`

export const FoldIcon = styled.span<FoldIconProps>`
  margin-right: 4px;
  font-size: 10px;
  transition: transform 0.2s;
  transform: rotate(${props => props.$folded ? '-90deg' : '0deg'});
`

export const ChannelSectionContent = styled.div<ChannelSectionContentProps>`
  display: ${props => props.$folded ? 'none' : 'block'};
`

export const PrivateChannelWrapper = styled(StyledChannelItem)<PrivateChannelWrapperProps>`
  height: ${props => props.hasSubtext ? '42px' : '34px'};
`

export const PrivateChannelAvatar = styled(AvatarContainer)`
  width: 32px;
  height: 32px;
  margin: 0 8px;
`

export const PrivateChannelName = styled.div<PrivateChannelNameProps>`
  font-size: 16px;
  line-height: 18px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: ${props => {
    if (props.$isActive) return props.theme.colors.text.primary;
    if (props.$hasUnread) return props.theme.colors.text.primary;
    return props.theme.colors.text.muted;
  }};
  font-weight: ${props => props.$hasUnread ? '600' : 'normal'};
`

export const HeaderStatusDot = styled(StatusDot)`
  border-color: ${theme.colors.background.primary};
`

export const AddChannelButton = styled.button`
  background: none;
  border: none;
  color: ${theme.colors.text.muted};
  font-size: 18px;
  padding: 0;
  cursor: pointer;
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s, color 0.2s;

  ${ChannelSectionHeader}:hover & {
    opacity: 1;
  }

  &:hover {
    color: ${theme.colors.text.primary};
  }
`

export const GroupAvatarContainer = styled(AvatarContainer)`
  width: 40px;
  height: 24px;
  margin: 0 8px;
`

export const ParticipantCount = styled.div`
  font-size: 12px;
  line-height: 13px;
  color: ${theme.colors.text.muted};
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`

export const TextContainer = styled.div`
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 2px;
`

export const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.85);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
`

export const ModalContainer = styled.div`
  background: ${theme.colors.background.primary};
  border-radius: 4px;
  width: 440px;
  max-width: 90%;
`

export const ModalHeader = styled.div`
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`

export const ModalTitle = styled.h2`
  color: ${theme.colors.text.primary};
  font-size: 20px;
  font-weight: 600;
  margin: 0;
`

export const ModalContent = styled.div`
  padding: 0 16px 16px;
`

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 16px;
`

export const InputGroup = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
`

export const Label = styled.label`
  color: ${theme.colors.text.primary};
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
`

export const Description = styled.p`
  color: ${theme.colors.text.muted};
  font-size: 14px;
  margin: -4px 0 0;
`

export const Input = styled.input`
  padding: 10px;
  background: ${theme.colors.background.tertiary};
  border: none;
  border-radius: 3px;
  color: ${theme.colors.text.primary};
  font-size: 16px;

  &:focus {
    outline: none;
    box-shadow: 0 0 0 2px ${theme.colors.status.online};
  }

  &::placeholder {
    color: ${theme.colors.text.muted};
  }
`

export const CreateButton = styled.button`
  margin-top: 8px;
  padding: 12px;
  background: ${theme.colors.status.online};
  color: white;
  border: none;
  border-radius: 3px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.2s;

  &:hover {
    background: #3ca374;
  }
`

export const CloseButton = styled.button`
  background: none;
  border: none;
  color: ${theme.colors.text.muted};
  font-size: 24px;
  cursor: pointer;
  padding: 4px;
  line-height: 1;

  &:hover {
    color: ${theme.colors.text.primary};
  }
`

export const SearchInput = styled(Input)`
  margin-bottom: 8px;
`

export const UserList = styled.div`
  max-height: 300px;
  overflow-y: auto;
  background: ${theme.colors.background.tertiary};
  border-radius: 4px;
`

export const UserItem = styled.div`
  display: flex;
  align-items: center;
  padding: 8px 12px;
  cursor: pointer;
  transition: background 0.2s;

  &:hover {
    background: ${theme.colors.background.hover};
  }

  & + & {
    border-top: 1px solid ${theme.colors.border.primary};
  }
`

export const Checkbox = styled.input`
  margin-right: 12px;
  width: 16px;
  height: 16px;
  cursor: pointer;
`

export const UserAvatar = styled.img`
  width: 32px;
  height: 32px;
  border-radius: 50%;
  margin-right: 12px;
`

export const UserInfo = styled.div`
  flex: 1;
  min-width: 0;
`

export const UserName = styled.div`
  color: ${theme.colors.text.primary};
  font-size: 14px;
  font-weight: 500;
`

export const UserEmail = styled.div`
  color: ${theme.colors.text.muted};
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`

export const NoResults = styled.div`
  padding: 16px;
  text-align: center;
  color: ${theme.colors.text.muted};
`

export const ErrorMessage = styled.div`
  color: ${theme.colors.status.error};
  font-size: 14px;
  padding: 8px 0;
  text-align: center;
  background-color: ${({ theme }) => theme.colors.background.tertiary};
  border-radius: 4px;
  margin-bottom: 8px;
` 