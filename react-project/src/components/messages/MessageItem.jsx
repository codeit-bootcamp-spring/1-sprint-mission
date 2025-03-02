import { useState } from "react";
import { useMessages } from "../../context/MessageContext";
import { formatDistanceToNow } from "date-fns";
import { ko } from "date-fns/locale";

const MessageItem = ({ message, isCurrentUser }) => {
  const { updateMessage, deleteMessage } = useMessages();
  const [isEditing, setIsEditing] = useState(false);
  const [editContent, setEditContent] = useState(message.content);
  const [showOptions, setShowOptions] = useState(false);

  const formattedTime = message.createAt
    ? formatDistanceToNow(new Date(message.createAt), {
        addSuffix: true,
        locale: ko,
      })
    : "";

  const handleEdit = () => {
    setEditContent(message.content);
    setIsEditing(true);
    setShowOptions(false);
  };

  const handleDelete = async () => {
    try {
      await deleteMessage(message.id);
    } catch (err) {
      console.error("Failed to delete message:", err);
    }
  };

  const handleSaveEdit = async () => {
    if (editContent.trim() === "") return;

    try {
      await updateMessage(message.id, editContent);
      setIsEditing(false);
    } catch (err) {
      console.error("Failed to update message:", err);
    }
  };

  const handleCancelEdit = () => {
    setIsEditing(false);
    setEditContent(message.content);
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSaveEdit();
    } else if (e.key === "Escape") {
      handleCancelEdit();
    }
  };

  return (
    <div
      className={`group relative rounded-lg p-2 hover:bg-gray-800 ${
        isCurrentUser ? "hover:bg-indigo-900/20" : ""
      }`}
      onMouseEnter={() => setShowOptions(true)}
      onMouseLeave={() => setShowOptions(false)}
    >
      <div className="flex">
        {/* User Avatar */}
        <div className="mr-3 h-10 w-10 flex-shrink-0 overflow-hidden rounded-full bg-gray-700">
          {message.writerProfileImgId ? (
            <img
              src={`/api/binaryContents/${message.writerProfileImgId}`}
              alt={message.writerName || "사용자"}
              className="h-full w-full object-cover"
            />
          ) : (
            <div className="flex h-full w-full items-center justify-center text-sm font-medium text-white">
              {(message.writerName || "?").charAt(0).toUpperCase()}
            </div>
          )}
        </div>

        {/* Message Content */}
        <div className="flex-1 overflow-hidden">
          <div className="flex items-center">
            <span className="font-medium text-white">
              {message.writerName || "알 수 없는 사용자"}
            </span>
            <span className="ml-2 text-xs text-gray-400">{formattedTime}</span>
          </div>

          {isEditing ? (
            <div className="mt-1">
              <textarea
                value={editContent}
                onChange={(e) => setEditContent(e.target.value)}
                onKeyDown={handleKeyDown}
                className="input w-full"
                rows={2}
                autoFocus
              />
              <div className="mt-2 flex justify-end space-x-2">
                <button
                  onClick={handleCancelEdit}
                  className="rounded px-2 py-1 text-xs text-gray-400 hover:bg-gray-700 hover:text-white"
                >
                  취소
                </button>
                <button
                  onClick={handleSaveEdit}
                  className="rounded bg-indigo-600 px-2 py-1 text-xs text-white hover:bg-indigo-700"
                >
                  저장
                </button>
              </div>
            </div>
          ) : (
            <div className="mt-1 whitespace-pre-wrap break-words text-gray-300">
              {message.content}
            </div>
          )}

          {/* Attachments */}
          {message.attachmentIdList && message.attachmentIdList.length > 0 && (
            <div className="mt-2 flex flex-wrap gap-2">
              {message.attachmentIdList.map((attachmentId) => (
                <a
                  key={attachmentId}
                  href={`/api/binaryContents/${attachmentId}`}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="inline-block rounded bg-gray-700 px-2 py-1 text-xs text-blue-400 hover:bg-gray-600 hover:text-blue-300"
                >
                  첨부 파일
                </a>
              ))}
            </div>
          )}
        </div>

        {/* Message Options */}
        {isCurrentUser && showOptions && !isEditing && (
          <div className="absolute right-2 top-2 flex space-x-1">
            <button
              onClick={handleEdit}
              className="rounded p-1 text-gray-400 hover:bg-gray-700 hover:text-white"
              title="수정"
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
                  d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"
                />
              </svg>
            </button>
            <button
              onClick={handleDelete}
              className="rounded p-1 text-gray-400 hover:bg-red-500 hover:text-white"
              title="삭제"
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
                  d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                />
              </svg>
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default MessageItem;
