import styled from 'styled-components'
import { theme } from './theme'

interface StatusDotProps {
  $online?: boolean;
  $background?: string;
}

interface InlineStatusDotProps {
  status?: string;
}

interface AvatarContainerProps {
  $size?: string;
  $margin?: string;
}

interface AvatarProps {
  $border?: string;
}

// 기존 StatusDot (아바타 위에 표시되는 상태 표시)
export const StatusDot = styled.div<StatusDotProps>`
  position: absolute;
  bottom: -3px;
  right: -3px;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: ${props => props.$online ? theme.colors.status.online : theme.colors.status.offline};
  border: 4px solid ${props => props.$background || theme.colors.background.secondary};
`

// 인라인으로 사용되는 상태 표시 (Member 컴포넌트용)
export const InlineStatusDot = styled.div<InlineStatusDotProps>`
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 8px;
  background: ${props => theme.colors.status[props.status || 'offline'] || theme.colors.status.offline};
`

// 기본 아바타 컨테이너 스타일
export const AvatarContainer = styled.div<AvatarContainerProps>`
  position: relative;
  width: ${props => props.$size || '32px'};
  height: ${props => props.$size || '32px'};
  flex-shrink: 0;
  margin: ${props => props.$margin || '0'};
`

// 기본 아바타 이미지 스타일
export const Avatar = styled.img<AvatarProps>`
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: ${props => props.$border || 'none'};
` 