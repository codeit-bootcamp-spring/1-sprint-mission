import { createContext, useState, useContext, useEffect } from "react";
import { channelAPI } from "../services/api";
import { useAuth } from "./AuthContext";

// Create the channel context
const ChannelContext = createContext();

// Custom hook to use the channel context
export const useChannels = () => {
  const context = useContext(ChannelContext);
  if (!context) {
    throw new Error("useChannels must be used within a ChannelProvider");
  }
  return context;
};

// Channel provider component
export const ChannelProvider = ({ children }) => {
  const { currentUser } = useAuth();
  const [channels, setChannels] = useState([]);
  const [selectedChannel, setSelectedChannel] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Fetch user's channels when user changes
  useEffect(() => {
    if (currentUser) {
      fetchUserChannels(currentUser.id);
    } else {
      setChannels([]);
      setSelectedChannel(null);
    }
  }, [currentUser]);

  // Fetch channels for a user
  const fetchUserChannels = async (userId) => {
    if (!userId) return;

    setLoading(true);
    setError(null);

    try {
      const response = await channelAPI.getUserChannels(userId);
      const fetchedChannels = response.data;
      setChannels(fetchedChannels);

      // If there are channels and no selected channel, select the first one
      if (fetchedChannels.length > 0 && !selectedChannel) {
        setSelectedChannel(fetchedChannels[0]);
      }
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "채널 목록을 불러오는데 실패했습니다.";
      setError(errorMessage);
      console.error("Failed to fetch channels:", err);
    } finally {
      setLoading(false);
    }
  };

  // Create a public channel
  const createPublicChannel = async (channelData) => {
    setLoading(true);
    setError(null);

    try {
      const response = await channelAPI.createPublicChannel(channelData);
      const newChannel = response.data;

      // Add the new channel to the list and select it
      setChannels((prevChannels) => [...prevChannels, newChannel]);
      setSelectedChannel(newChannel);

      return newChannel;
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "공개 채널 생성에 실패했습니다.";
      setError(errorMessage);
      throw new Error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Create a private channel
  const createPrivateChannel = async (participantIds) => {
    setLoading(true);
    setError(null);

    try {
      const response = await channelAPI.createPrivateChannel({
        participantIds,
      });
      const newChannel = response.data;

      // Add the new channel to the list and select it
      setChannels((prevChannels) => [...prevChannels, newChannel]);
      setSelectedChannel(newChannel);

      return newChannel;
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "비공개 채널 생성에 실패했습니다.";
      setError(errorMessage);
      throw new Error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Update a channel
  const updateChannel = async (channelId, channelData) => {
    setLoading(true);
    setError(null);

    try {
      const response = await channelAPI.updateChannel(channelId, channelData);
      const updatedChannel = response.data;

      // Update the channel in the list
      setChannels((prevChannels) =>
        prevChannels.map((channel) =>
          channel.id === channelId ? updatedChannel : channel
        )
      );

      // Update selected channel if it's the one being updated
      if (selectedChannel && selectedChannel.id === channelId) {
        setSelectedChannel(updatedChannel);
      }

      return updatedChannel;
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "채널 업데이트에 실패했습니다.";
      setError(errorMessage);
      throw new Error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Delete a channel
  const deleteChannel = async (channelId) => {
    setLoading(true);
    setError(null);

    try {
      await channelAPI.deleteChannel(channelId);

      // Remove the channel from the list
      const updatedChannels = channels.filter(
        (channel) => channel.id !== channelId
      );
      setChannels(updatedChannels);

      // If the deleted channel was selected, select another one
      if (selectedChannel && selectedChannel.id === channelId) {
        setSelectedChannel(
          updatedChannels.length > 0 ? updatedChannels[0] : null
        );
      }

      return true;
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "채널 삭제에 실패했습니다.";
      setError(errorMessage);
      throw new Error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Select a channel
  const selectChannel = (channel) => {
    setSelectedChannel(channel);
  };

  // Value to be provided by the context
  const value = {
    channels,
    selectedChannel,
    loading,
    error,
    fetchUserChannels,
    createPublicChannel,
    createPrivateChannel,
    updateChannel,
    deleteChannel,
    selectChannel,
  };

  return (
    <ChannelContext.Provider value={value}>{children}</ChannelContext.Provider>
  );
};

export default ChannelContext;
