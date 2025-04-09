import { theme } from '@/styles/theme'
import styled from 'styled-components'
import { Avatar, AvatarContainer } from '@/styles/common'

export const StyledMemberList = styled.div`
  width: 240px;
  background: ${theme.colors.background.secondary};
  border-left: 1px solid ${theme.colors.border.primary};
`

export const MemberHeader = styled.div`
  padding: 16px;
  font-size: 14px;
  font-weight: bold;
  color: ${theme.colors.text.muted};
  text-transform: uppercase;
`

export const StyledMemberItem = styled.div`
  padding: 8px 16px;
  display: flex;
  align-items: center;
  color: ${theme.colors.text.muted};
`

export const MemberAvatarContainer = styled(AvatarContainer)`
  margin-right: 12px;
`

export const MemberAvatar = styled(Avatar)``

export const MemberInfo = styled.div`
  display: flex;
  align-items: center;
` 