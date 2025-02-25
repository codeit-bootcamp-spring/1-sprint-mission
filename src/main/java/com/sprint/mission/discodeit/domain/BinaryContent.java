package com.sprint.mission.discodeit.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private Instant createdAt;
    private byte[] content;
    private String filePath;
    private String contentType;
    private String originalFileName;
    private String storedFileName;

    public BinaryContent(byte[] content, String filePath, String contentType, String originalFileName, String storedFileName) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.content = content;
        this.contentType = contentType;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.filePath = filePath;
    }
}
