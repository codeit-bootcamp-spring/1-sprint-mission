import { useState } from "react";
import { useChannels } from "../../context/ChannelContext";

const CreateChannelModal = ({ onClose }) => {
  const { createPublicChannel, loading, error } = useChannels();
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [localError, setLocalError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLocalError("");

    // Validate inputs
    if (!name.trim()) {
      setLocalError("채널 이름을 입력해주세요.");
      return;
    }

    if (name.length < 2 || name.length > 10) {
      setLocalError("채널 이름은 2~10자 사이여야 합니다.");
      return;
    }

    try {
      await createPublicChannel({ name, description });
      onClose();
    } catch (err) {
      // Error is handled by the channel context
      console.error("Channel creation error:", err);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
      <div className="w-full max-w-md rounded-lg bg-gray-800 p-6 shadow-xl">
        <div className="mb-4 flex items-center justify-between">
          <h2 className="text-xl font-bold text-white">새 채널 만들기</h2>
          <button
            onClick={onClose}
            className="rounded-full p-1 text-gray-400 hover:bg-gray-700 hover:text-white"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>

        {(error || localError) && (
          <div className="mb-4 rounded-md bg-red-500 bg-opacity-10 p-3 text-sm text-red-400">
            {error || localError}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label
              htmlFor="channelName"
              className="mb-2 block text-sm font-medium text-gray-300"
            >
              채널 이름
            </label>
            <input
              type="text"
              id="channelName"
              className="input w-full"
              placeholder="2~10자 사이"
              value={name}
              onChange={(e) => setName(e.target.value)}
              maxLength={10}
            />
          </div>

          <div className="mb-6">
            <label
              htmlFor="channelDescription"
              className="mb-2 block text-sm font-medium text-gray-300"
            >
              채널 설명 (선택사항)
            </label>
            <textarea
              id="channelDescription"
              className="input w-full"
              placeholder="채널의 주제나 목적을 설명해주세요"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              rows={3}
            />
          </div>

          <div className="flex justify-end space-x-3">
            <button
              type="button"
              onClick={onClose}
              className="btn-secondary btn"
              disabled={loading}
            >
              취소
            </button>
            <button
              type="submit"
              className="btn-primary btn"
              disabled={loading}
            >
              {loading ? "생성 중..." : "채널 생성"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateChannelModal;
