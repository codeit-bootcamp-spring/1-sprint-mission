package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;

    private UUID userId;
    private UUID messageId;
    private String fileName;
    private String mimeType;
    private byte[] data;

    public BinaryContent(UUID userId, UUID messageId, String mimeType, String fileName, byte[] content) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.userId = userId;
        this.messageId = messageId;

        this.fileName = fileName;
        this.mimeType = mimeType;
        this.data = content.clone();
    }
}
