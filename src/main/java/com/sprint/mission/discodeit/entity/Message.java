package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity{
    private String content;
    private final UUID userId;
    private final UUID channelId;
    public Message(String content, UUID userId, UUID channelId) {
        super();
        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
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

    public void updateContent(String message) {
        this.content = content;
        setUpdatedAt(System.currentTimeMillis());
    }
}
