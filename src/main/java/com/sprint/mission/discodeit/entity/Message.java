package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    private String content;
    private final String userId;
    private final String channelId;

    public Message(String content, String userId, String channelId) {
        super();
        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public String getUserId() {
        return userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void updateContent(String content) {
        this.content = content;
        setUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Message{id=" + getId() + ", content='" + content + "', userId=" + userId + ", channelId=" + channelId + "}";
    }
}
