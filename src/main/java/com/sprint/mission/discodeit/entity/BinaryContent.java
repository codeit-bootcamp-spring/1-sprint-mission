package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID userId;
    private final UUID messageId;
    private final String fileName;
    private final String fileType;
    private final byte[] data;
    private final Instant createdAt;

    public BinaryContent(UUID userId, UUID messageId, String fileName, String fileType, byte[] data) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.messageId = messageId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.createdAt = Instant.now();
    }
}