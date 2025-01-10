package com.sprint.mission.discodeit.entity;
import java.util.UUID;

public class Message extends BaseEntity {
    private String content;
    private UUID userId;
    private UUID channelId;

    public Message(String content, UUID userId, UUID channelId) {
        super();
        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        update();
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    @Override
    public String toString() {
        return "Message{id=" + id + ", content='" + content + '\'' + ", userId=" + userId + ", channelId=" + channelId + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}
