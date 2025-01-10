package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    // 메시지 내용
    private String content;
    // 작성한 사용자 (User)
    private UUID userId;
    // 메시지가 속한 채널 (Channel)
    private UUID channelId;

    public Message(String content, UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;

        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
    }

    public UUID getId() {
        return id;
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

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = System.currentTimeMillis();
    }
}
