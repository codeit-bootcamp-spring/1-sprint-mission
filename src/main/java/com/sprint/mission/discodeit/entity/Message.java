package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final UUID authorId;
    private final UUID channelId;
    private final long createdAt;
    private long updatedAt;
    private String content;

    public Message(UUID channelId, UUID authorId, String content) {
        this.id = UUID.randomUUID();
        this.authorId = authorId;
        this.channelId = channelId;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID channelId() {
        return channelId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = System.currentTimeMillis();
    }
}
