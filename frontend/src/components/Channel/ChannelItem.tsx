import defaultProfile from '@/assets/default_profile.png';
import useBinaryContentStore from '@/stores/binaryContentStore';
import useUserStore from '@/stores/userStore';
import { ChannelDto } from '@/types/api';
import { Avatar, StatusDot } from '../../styles/common';
import {
    GroupAvatarContainer,
    ParticipantCount,
    PrivateChannelAvatar,
    PrivateChannelName,
    PrivateChannelWrapper,
    StyledChannelItem,
    TextContainer,
} from './styles';

interface ChannelItemProps {
  channel: ChannelDto;
  isActive: boolean;
  onClick: () => void;
  hasUnread: boolean;
}

export function ChannelItem({ channel, isActive, onClick, hasUnread }: ChannelItemProps): JSX.Element {
  const currentUser = useUserStore((state) => state.currentUserId);
  const { binaryContents } = useBinaryContentStore();

  if (channel.type === 'PUBLIC') {
    return (
      <StyledChannelItem $isActive={isActive} onClick={onClick} $hasUnread={hasUnread}>
        # {channel.name}
      </StyledChannelItem>
    );
  }

  const participants = channel.participants;
  // 그룹 채팅인 경우
  if (participants.length > 2) {
    const usernames = participants.filter(p => p.id !== currentUser).map(p => p.username).join(', ');
    return (
      <PrivateChannelWrapper $isActive={isActive} onClick={onClick}>
        <GroupAvatarContainer>
          {participants.filter(p => p.id !== currentUser).slice(0, 2).map((participant, index) => (
            <Avatar 
              key={participant.id}
              src={participant.profile ? binaryContents[participant.profile.id]?.url : defaultProfile}
              style={{ 
                position: 'absolute',
                left: index * 16,
                zIndex: 2 - index,
                width: '24px',
                height: '24px',
                border: '2px solid #2a2a2a'
              }}
            />
          ))}
        </GroupAvatarContainer>
        <TextContainer>
          <PrivateChannelName $hasUnread={hasUnread}>{usernames}</PrivateChannelName>
          <ParticipantCount>멤버 {participants.length}명</ParticipantCount>
        </TextContainer>
      </PrivateChannelWrapper>
    );
  }

  // 1:1 채팅인 경우
  const participant = participants.filter(p => p.id !== currentUser)[0];
  return (
    participant && (
    <PrivateChannelWrapper $isActive={isActive} onClick={onClick}>
      <PrivateChannelAvatar>
        <Avatar 
          src={participant.profile ? binaryContents[participant.profile.id]?.url : defaultProfile} 
          alt="profile" 
        />
        <StatusDot $online={participant.online} />
      </PrivateChannelAvatar>
      <TextContainer>
        <PrivateChannelName $hasUnread={hasUnread}>{participant.username}</PrivateChannelName>
      </TextContainer>
    </PrivateChannelWrapper>
  ));
} 