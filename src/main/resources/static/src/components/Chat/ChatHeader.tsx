import defaultProfile from '@/assets/default_profile.png';
import useBinaryContentStore from '@/stores/binaryContentStore';
import useUserListStore from '@/stores/userListStore';
import useUserStore from '@/stores/userStore';
import { ChannelDto, UserDto } from '@/types/api';
import { Avatar } from '../../styles/common';
import {
    GroupAvatarContainer,
    HeaderChannelName,
    HeaderLeft,
    HeaderPrivateInfo,
    HeaderStatusDot,
    ParticipantCount,
    PrivateAvatarContainer,
    StyledChatHeader
} from './styles';

interface ChatHeaderProps {
  channel: ChannelDto;
}

function ChatHeader({ channel }: ChatHeaderProps): JSX.Element | null {
  const currentUser = useUserStore((state) => state.currentUserId);
  const users = useUserListStore((state) => state.users);
  const binaryContents = useBinaryContentStore((state) => state.binaryContents);

  if (!channel) return null;

  // PUBLIC 채널
  if (channel.type === 'PUBLIC') {
    return (
      <StyledChatHeader>
        <HeaderLeft>
          <HeaderChannelName># {channel.name}</HeaderChannelName>
        </HeaderLeft>
      </StyledChatHeader>
    );
  }

  // participants를 사용하여 실제 사용자 정보 가져오기
  const participants = channel.participants
    .map(participant => users.find(user => user.id === participant.id))
    .filter(Boolean) as UserDto[];

  const filteredParticipants = participants.filter(p => p.id !== currentUser);

  // PRIVATE 채널
  const isGroup = participants.length > 2;
  const usernames = participants.filter(p => p.id !== currentUser).map(p => p.username).join(', ');

  return (
    <StyledChatHeader>
      <HeaderLeft>
        <HeaderPrivateInfo>
          {isGroup ? (
            // 그룹 채팅
            <GroupAvatarContainer>
              {filteredParticipants.slice(0, 2).map((participant, index) => (
                <Avatar 
                  key={participant.id}
                  src={participant.profile ? binaryContents[participant.profile.id]?.url : defaultProfile}
                  style={{ 
                    position: 'absolute',
                    left: index * 16,
                    zIndex: 2 - index,
                    width: '24px',
                    height: '24px'
                  }}
                />
              ))}
            </GroupAvatarContainer>
          ) : (
            // 1:1 채팅
            <PrivateAvatarContainer>
              <Avatar 
                src={filteredParticipants[0].profile ? binaryContents[filteredParticipants[0].profile.id]?.url : defaultProfile} 
              />
              <HeaderStatusDot $online={filteredParticipants[0].online} />
            </PrivateAvatarContainer>
          )}
          <div>
            <HeaderChannelName>{usernames}</HeaderChannelName>
            {isGroup && (
              <ParticipantCount>멤버 {participants.length}명</ParticipantCount>
            )}
          </div>
        </HeaderPrivateInfo>
      </HeaderLeft>
    </StyledChatHeader>
  );
}

export default ChatHeader; 