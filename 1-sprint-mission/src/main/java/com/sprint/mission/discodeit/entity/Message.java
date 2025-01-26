package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private UUID id;
    private String content;
    private UUID userId;
    private UUID channelId;

    public Message(String content, UUID userId, UUID channelId) {
        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
    }

    public Message() {} // 직렬화용

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }
    public UUID getUserId() {
        return userId;
    }
    public UUID getChannelId() {
        return channelId;
    }

    public void update(String newContent) {
        this.content = newContent;
    }
}
