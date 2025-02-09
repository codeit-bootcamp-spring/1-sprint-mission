package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class BinaryContent {

    private final UUID id;
    private final Instant createdAt;

    private UUID userId;
    private UUID messageId;
    private byte[] data;  // 바이너리 데이터 저장
    private String contentType;
    private Long size;


    public BinaryContent(UUID userId, UUID messageId, byte[] data, String contentType, Long size) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.messageId = messageId;
        this.data = data;
        this.contentType = contentType;
        this.size =size;
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public byte[] getData() {
        return data;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getSize() {
        return size;
    }
}
