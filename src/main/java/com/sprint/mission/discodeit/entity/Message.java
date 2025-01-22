package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final String username;
    private final UUID channelId;
    private String content; //변경 가능해야해서 final이 아님
    private final long createdAt;
    private long updatedAt;

    public Message(String username, UUID channelId, String content) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.channelId = channelId;
        this.content = content;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public String getContent() {
        return content;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = System.currentTimeMillis();
    }
}
