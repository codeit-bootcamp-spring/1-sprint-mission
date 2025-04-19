import { createContext, useState, useContext, useEffect } from "react";
import { messageAPI, readStatusAPI } from "../services/api";
import { useAuth } from "./AuthContext";
import { useChannels } from "./ChannelContext";

// Create the message context
const MessageContext = createContext();

// Custom hook to use the message context
export const useMessages = () => {
  const context = useContext(MessageContext);
  if (!context) {
    throw new Error("useMessages must be used within a MessageProvider");
  }
  return context;
};

// Message provider component
export const MessageProvider = ({ children }) => {
  const { currentUser } = useAuth();
  const { selectedChannel } = useChannels();
  const [messages, setMessages] = useState([]);
  const [readStatus, setReadStatus] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Fetch messages when selected channel changes
  useEffect(() => {
    if (selectedChannel) {
      fetchChannelMessages(selectedChannel.id);
      if (currentUser) {
        fetchOrCreateReadStatus(currentUser.id, selectedChannel.id);
      }
    } else {
      setMessages([]);
      setReadStatus(null);
    }
  }, [selectedChannel, currentUser]);

  // Fetch messages for a channel
  const fetchChannelMessages = async (channelId) => {
    if (!channelId) return;

    setLoading(true);
    setError(null);

    try {
      const response = await messageAPI.getChannelMessages(channelId);
      setMessages(response.data);
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "메시지를 불러오는데 실패했습니다.";
      setError(errorMessage);
      console.error("Failed to fetch messages:", err);
    } finally {
      setLoading(false);
    }
  };

  // Fetch or create read status for a user and channel
  const fetchOrCreateReadStatus = async (userId, channelId) => {
    if (!userId || !channelId) return;

    try {
      // First try to fetch existing read status
      const response = await readStatusAPI.getUserReadStatuses(userId);
      const statuses = response.data;

      // Find read status for the current channel
      const channelReadStatus = statuses.find(
        (status) => status.channelId === channelId
      );

      if (channelReadStatus) {
        setReadStatus(channelReadStatus);
        // Update the read status to current time
        updateReadStatus(channelReadStatus.id, new Date().toISOString());
      } else {
        // Create new read status if none exists
        const newReadStatus = {
          userId,
          channelId,
          lastReadAt: new Date().toISOString(),
        };

        const createResponse = await readStatusAPI.createReadStatus(
          newReadStatus
        );
        setReadStatus(createResponse.data);
      }
    } catch (err) {
      console.error("Failed to fetch or create read status:", err);
    }
  };

  // Update read status
  const updateReadStatus = async (readStatusId, newLastReadAt) => {
    if (!readStatusId) return;

    try {
      const response = await readStatusAPI.updateReadStatus(readStatusId, {
        newLastReadAt,
      });
      setReadStatus(response.data);
    } catch (err) {
      console.error("Failed to update read status:", err);
    }
  };

  // Send a message
  const sendMessage = async (content, attachments = []) => {
    if (!currentUser || !selectedChannel) return;

    setLoading(true);
    setError(null);

    try {
      const messageData = {
        userId: currentUser.id,
        channelId: selectedChannel.id,
        content,
      };

      const response = await messageAPI.createMessage(messageData, attachments);
      const newMessage = response.data;

      // Add the new message to the list
      setMessages((prevMessages) => [...prevMessages, newMessage]);

      // Update read status
      if (readStatus) {
        updateReadStatus(readStatus.id, new Date().toISOString());
      }

      return newMessage;
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "메시지 전송에 실패했습니다.";
      setError(errorMessage);
      throw new Error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Update a message
  const updateMessage = async (messageId, newContent) => {
    setLoading(true);
    setError(null);

    try {
      const response = await messageAPI.updateMessage(messageId, newContent);
      const updatedMessage = response.data;

      // Update the message in the list
      setMessages((prevMessages) =>
        prevMessages.map((message) =>
          message.id === messageId ? updatedMessage : message
        )
      );

      return updatedMessage;
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "메시지 수정에 실패했습니다.";
      setError(errorMessage);
      throw new Error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Delete a message
  const deleteMessage = async (messageId) => {
    setLoading(true);
    setError(null);

    try {
      await messageAPI.deleteMessage(messageId);

      // Remove the message from the list
      setMessages((prevMessages) =>
        prevMessages.filter((message) => message.id !== messageId)
      );

      return true;
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "메시지 삭제에 실패했습니다.";
      setError(errorMessage);
      throw new Error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Value to be provided by the context
  const value = {
    messages,
    readStatus,
    loading,
    error,
    fetchChannelMessages,
    sendMessage,
    updateMessage,
    deleteMessage,
    updateReadStatus,
  };

  return (
    <MessageContext.Provider value={value}>{children}</MessageContext.Provider>
  );
};

export default MessageContext;
