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

    // 각 필드의 Getter 함수 정의
    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    // 현재는 사용하지 않는 값이지만, 확장성을 위해 모든 필드에 대해 Getter 함수 작성
    public UUID getSenderId() {
        return senderId;
    }

    public UUID getChannelId() {
        return channelId;
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
