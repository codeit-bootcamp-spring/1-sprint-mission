package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String content;
    private UUID senderId;
    private UUID channelId;

    public Message(String content, UUID senderId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    public UUID getId() { return id; }
    public long getCreatedAt() { return createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public String getContent() { return content; }
    public UUID getSenderId() { return senderId; }
    public UUID getChannelId() { return channelId; }

    public void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = System.currentTimeMillis();
    }
}
