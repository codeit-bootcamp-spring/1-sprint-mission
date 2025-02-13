package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;

    private String fileName;
    private Long size;
    private String contentType;
    private byte[] bytes;

    public BinaryContent() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}