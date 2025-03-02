import { useEffect, useRef } from "react";
import { useChannels } from "../../context/ChannelContext";
import { useMessages } from "../../context/MessageContext";
import { useAuth } from "../../context/AuthContext";
import MessageItem from "./MessageItem";
import MessageInput from "./MessageInput";

const ChatArea = () => {
  const { selectedChannel } = useChannels();
  const { messages, loading, error } = useMessages();
  const { currentUser } = useAuth();
  const messagesEndRef = useRef(null);

  // Scroll to bottom when messages change
  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages]);

  if (!selectedChannel) {
    return (
      <div className="flex h-screen flex-1 flex-col items-center justify-center bg-gray-900 p-6">
        <div className="text-center">
          <h2 className="mb-2 text-2xl font-bold text-white">
            채널을 선택해주세요
          </h2>
          <p className="text-gray-400">
            왼쪽 사이드바에서 채널을 선택하거나 새 채널을 만들어보세요.
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen flex-1 flex-col bg-gray-900">
      {/* Channel Header */}
      <div className="flex h-12 items-center justify-between border-b border-gray-800 px-4 py-2">
        <div className="flex items-center">
          <span className="mr-2 text-gray-400">
            {selectedChannel.channelType === "PUBLIC" ? "#" : "@"}
          </span>
          <h2 className="text-lg font-medium text-white">
            {selectedChannel.name || "비공개 대화"}
          </h2>
        </div>
        {selectedChannel.description && (
          <div className="hidden text-sm text-gray-400 md:block">
            {selectedChannel.description}
          </div>
        )}
      </div>

      {/* Messages Area */}
      <div className="flex-1 overflow-y-auto p-4">
        {loading ? (
          <div className="flex h-full items-center justify-center">
            <div className="text-center text-gray-400">메시지 로딩 중...</div>
          </div>
        ) : error ? (
          <div className="flex h-full items-center justify-center">
            <div className="text-center text-red-400">{error}</div>
          </div>
        ) : messages.length === 0 ? (
          <div className="flex h-full items-center justify-center">
            <div className="text-center">
              <h3 className="mb-2 text-xl font-bold text-white">
                {selectedChannel.channelType === "PUBLIC"
                  ? `${selectedChannel.name} 채널에 오신 것을 환영합니다!`
                  : "대화를 시작해보세요!"}
              </h3>
              <p className="text-gray-400">이 채널의 첫 메시지를 보내보세요.</p>
            </div>
          </div>
        ) : (
          <div className="space-y-4">
            {messages.map((message) => (
              <MessageItem
                key={message.id}
                message={message}
                isCurrentUser={message.writerId === currentUser?.id}
              />
            ))}
            <div ref={messagesEndRef} />
          </div>
        )}
      </div>

      {/* Message Input */}
      <div className="border-t border-gray-800 p-4">
        <MessageInput />
      </div>
    </div>
  );
};

export default ChatArea;
