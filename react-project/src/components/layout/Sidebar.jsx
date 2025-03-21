import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useChannels } from "../../context/ChannelContext";
import UserStatus from "../ui/UserStatus";
import CreateChannelModal from "../channels/CreateChannelModal";

const Sidebar = () => {
  const { currentUser, logout } = useAuth();
  const { channels, selectedChannel, selectChannel, loading } = useChannels();
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

  const handleLogout = async () => {
    try {
      await logout();
    } catch (err) {
      console.error("Logout error:", err);
    }
  };

  const handleChannelSelect = (channel) => {
    selectChannel(channel);
  };

  const handleCreateChannel = () => {
    setIsCreateModalOpen(true);
  };

  const closeCreateModal = () => {
    setIsCreateModalOpen(false);
  };

  // Group channels by type
  const publicChannels = channels.filter(
    (channel) => channel.channelType === "PUBLIC"
  );
  const privateChannels = channels.filter(
    (channel) => channel.channelType === "PRIVATE"
  );

  return (
    <div className="flex h-screen w-64 flex-col bg-gray-800">
      {/* Server Header */}
      <div className="flex h-12 items-center justify-between border-b border-gray-700 px-4">
        <h1 className="text-lg font-bold text-white">Discodeit</h1>
      </div>

      {/* Channels */}
      <div className="flex-1 overflow-y-auto p-3">
        {/* Public Channels */}
        <div className="mb-4">
          <div className="mb-2 flex items-center justify-between">
            <h2 className="text-xs font-semibold uppercase tracking-wider text-gray-400">
              공개 채널
            </h2>
            <button
              onClick={handleCreateChannel}
              className="rounded p-1 text-gray-400 hover:bg-gray-700 hover:text-white"
              title="채널 생성"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-4 w-4"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M12 4v16m8-8H4"
                />
              </svg>
            </button>
          </div>

          {loading ? (
            <div className="py-2 text-center text-sm text-gray-400">
              로딩 중...
            </div>
          ) : publicChannels.length === 0 ? (
            <div className="py-2 text-center text-sm text-gray-400">
              공개 채널이 없습니다
            </div>
          ) : (
            <ul className="space-y-1">
              {publicChannels.map((channel) => (
                <li key={channel.id}>
                  <button
                    onClick={() => handleChannelSelect(channel)}
                    className={`flex w-full items-center rounded px-2 py-1 text-left text-sm ${
                      selectedChannel?.id === channel.id
                        ? "bg-gray-700 text-white"
                        : "text-gray-300 hover:bg-gray-700 hover:text-white"
                    }`}
                  >
                    <span className="mr-1 text-gray-400">#</span>
                    <span className="truncate">{channel.name}</span>
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>

        {/* Private Channels */}
        <div>
          <h2 className="mb-2 text-xs font-semibold uppercase tracking-wider text-gray-400">
            비공개 메시지
          </h2>
          {loading ? (
            <div className="py-2 text-center text-sm text-gray-400">
              로딩 중...
            </div>
          ) : privateChannels.length === 0 ? (
            <div className="py-2 text-center text-sm text-gray-400">
              비공개 메시지가 없습니다
            </div>
          ) : (
            <ul className="space-y-1">
              {privateChannels.map((channel) => (
                <li key={channel.id}>
                  <button
                    onClick={() => handleChannelSelect(channel)}
                    className={`flex w-full items-center rounded px-2 py-1 text-left text-sm ${
                      selectedChannel?.id === channel.id
                        ? "bg-gray-700 text-white"
                        : "text-gray-300 hover:bg-gray-700 hover:text-white"
                    }`}
                  >
                    <span className="mr-1 text-gray-400">@</span>
                    <span className="truncate">
                      {channel.name || "비공개 대화"}
                    </span>
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>

      {/* User Panel */}
      {currentUser && (
        <div className="flex items-center justify-between border-t border-gray-700 bg-gray-700 p-3">
          <div className="flex items-center">
            <div className="relative h-8 w-8 overflow-hidden rounded-full bg-gray-600">
              {currentUser.profileImgId ? (
                <img
                  src={`/api/binaryContents/${currentUser.profileImgId}`}
                  alt={currentUser.username}
                  className="h-full w-full object-cover"
                />
              ) : (
                <div className="flex h-full w-full items-center justify-center text-sm font-medium text-white">
                  {currentUser.username.charAt(0).toUpperCase()}
                </div>
              )}
              <UserStatus
                isOnline={true}
                className="absolute bottom-0 right-0"
              />
            </div>
            <div className="ml-2">
              <div className="text-sm font-medium text-white">
                {currentUser.username}
              </div>
              <div className="text-xs text-gray-400">온라인</div>
            </div>
          </div>
          <button
            onClick={handleLogout}
            className="rounded p-1 text-gray-400 hover:bg-gray-600 hover:text-white"
            title="로그아웃"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-5 w-5"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"
              />
            </svg>
          </button>
        </div>
      )}

      {/* Create Channel Modal */}
      {isCreateModalOpen && <CreateChannelModal onClose={closeCreateModal} />}
    </div>
  );
};

export default Sidebar;
