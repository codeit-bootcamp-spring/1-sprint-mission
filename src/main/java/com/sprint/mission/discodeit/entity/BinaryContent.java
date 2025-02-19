package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Long createdAt;
    private final String fileName;
    private final String mimeType;
    private final String filePath;

    public BinaryContent(String fileName, String mimeType, String filePath) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().toEpochMilli();
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.filePath = filePath;
    }
}
