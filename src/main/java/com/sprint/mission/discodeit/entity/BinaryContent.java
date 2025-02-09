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


    public BinaryContent(UUID userId, UUID messageId, byte[] data, String contentType) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.messageId = messageId;
        this.data = data;
        this.contentType = contentType;
    }
}
