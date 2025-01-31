package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String content;
    private UUID senderId;
    private UUID channelId;

    public Message(UUID uuid, String content, UUID senderId, UUID channelId) {
        // 유효성 검사 추가
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("메시지 내용이 비어 있습니다.");
        }
        if (senderId == null) {
            throw new IllegalArgumentException("발신자 ID가 비어 있습니다.");
        }
        if (channelId == null) {
            throw new IllegalArgumentException("채널 ID가 비어 있습니다.");
        }

        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    // 각 필드의 Getter 함수 정의
    public UUID getId() { return id; }
    public String getContent() { return content; }
    public UUID getSenderId() { return senderId; }
    public UUID getChannelId() { return channelId; }
    public long getCreatedAt() { return createdAt; }
    public long getUpdatedAt() { return updatedAt; }

    public void setContent(String content) {
        this.content = content;
    }

    public void updateContent(String newContent) {
        // 업데이트 시에도 유효성 검사 추가
        if (newContent == null || newContent.trim().isEmpty()) {
            throw new IllegalArgumentException("새 메시지 내용이 비어있습니다.");
        }
        this.content = newContent;
        this.updatedAt = System.currentTimeMillis();
    }
}
