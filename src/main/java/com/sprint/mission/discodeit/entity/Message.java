package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Message extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private String content;
    private final UUID userId;
    private final UUID channelId;

    public Message(UUID id, String content, UUID userId, UUID channelId) {
        super();
        this.id = id;
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