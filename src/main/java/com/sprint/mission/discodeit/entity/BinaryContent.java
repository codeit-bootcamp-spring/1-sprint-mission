package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;

    private String fileName;
    private Long size;
    private String contentType;
    private byte[] data;

    public BinaryContent(String fileName, Long size, String contentType, byte[] data) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.data = data;
    }

    public boolean containsId(List<UUID> ids) {
        return ids.contains(this.id);
    }
}
