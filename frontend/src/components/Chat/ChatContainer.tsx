import { ChannelDto } from '@/types/api';
import ChatHeader from './ChatHeader';
import MessageInput from './MessageInput';
import MessageList from './MessageList';
import {
    EmptyContainer,
    EmptyContent,
    StyledChatContainer,
    WelcomeIcon,
    WelcomeText,
    WelcomeTitle
} from './styles';

interface ChatContainerProps {
  channel: ChannelDto | null;
}

function ChatContainer({ channel }: ChatContainerProps): JSX.Element {
  if (!channel) {
    return (
      <EmptyContainer>
        <EmptyContent>
          <WelcomeIcon>👋</WelcomeIcon>
          <WelcomeTitle>채널을 선택해주세요</WelcomeTitle>
          <WelcomeText>
            왼쪽의 채널 목록에서 채널을 선택하여<br />
            대화를 시작하세요.
          </WelcomeText>
        </EmptyContent>
      </EmptyContainer>
    );
  }

  return (
    <StyledChatContainer>
      <ChatHeader channel={channel} />
      <MessageList channel={channel} />
      <MessageInput channel={channel} />
    </StyledChatContainer>
  );
}

export default ChatContainer; 