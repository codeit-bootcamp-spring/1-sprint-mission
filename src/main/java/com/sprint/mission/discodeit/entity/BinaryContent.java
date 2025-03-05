package com.sprint.mission.discodeit.entity;

import lombok.Data;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Data
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private final String fileName;
    private final String mimeType;
    private final String filePath;
    private Long size;
    private byte[] bytes;

    public BinaryContent(String fileName, String mimeType, String filePath, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.filePath = filePath;
        this.bytes = bytes;
    }

    public BinaryContent(UUID id, String fileName, String filePath, String mimeType, byte[] data) {
        this.id = id;
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.filePath = filePath;
        this.bytes = data;
    }

    public BinaryContent(String fileName, long length, String contentType, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.mimeType = contentType;
        this.filePath = fileName;
        this.bytes = bytes;
        this.size = length;
    }
}
