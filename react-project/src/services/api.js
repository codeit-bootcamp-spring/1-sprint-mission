import axios from "axios";

// Create an axios instance with default config
const api = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// Auth API
export const authAPI = {
  login: (loginRequest) => api.get("/auth/login", { params: loginRequest }),
};

// User API
export const userAPI = {
  getAllUsers: () => api.get("/users"),

  createUser: (userData, profileImage) => {
    const formData = new FormData();
    const createRequestDto = new Blob([JSON.stringify(userData)], {
      type: "application/json",
    });

    formData.append("createRequestDto", createRequestDto);

    if (profileImage) {
      formData.append("profile", profileImage);
    }

    return api.post("/users", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  updateUser: (userId, userData) => api.patch(`/users/${userId}`, userData),

  deleteUser: (userId) => api.delete(`/users/${userId}`),

  updateUserStatus: (userId) => api.patch(`/users/${userId}/status`),
};

// Channel API
export const channelAPI = {
  getUserChannels: (userId) => api.get("/channels", { params: { userId } }),

  createPublicChannel: (channelData) =>
    api.post("/channels/public", channelData),

  createPrivateChannel: (participantIds) =>
    api.post("/channels/private", { participantIds }),

  updateChannel: (channelId, channelData) =>
    api.patch(`/channels/${channelId}`, channelData),

  deleteChannel: (channelId) => api.delete(`/channels/${channelId}`),
};

// Message API
export const messageAPI = {
  getChannelMessages: (channelId) =>
    api.get("/messages", { params: { channelId } }),

  createMessage: (messageData, attachments = []) => {
    const formData = new FormData();
    const messageCreateDto = new Blob([JSON.stringify(messageData)], {
      type: "application/json",
    });

    formData.append("messageCreateDto", messageCreateDto);

    if (attachments.length > 0) {
      attachments.forEach((file) => {
        formData.append("attachments", file);
      });
    }

    return api.post("/messages", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  updateMessage: (messageId, newContent) =>
    api.patch(`/messages/${messageId}`, { newContent }),

  deleteMessage: (messageId) =>
    api.delete(`/messages/${messageId}`, { params: { id: messageId } }),
};

// ReadStatus API
export const readStatusAPI = {
  getUserReadStatuses: (userId) =>
    api.get("/readStatuses", { params: { userId } }),

  createReadStatus: (readStatusData) =>
    api.post("/readStatuses", readStatusData),

  updateReadStatus: (readStatusId, newLastReadAt) =>
    api.patch(`/readStatuses/${readStatusId}`, { newLastReadAt }),
};

// BinaryContent API
export const binaryContentAPI = {
  getBinaryContent: (id) => api.get(`/binaryContents/${id}`),

  getBinaryContents: (ids) => api.get("/binaryContents", { params: { ids } }),
};

export default api;
