package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long getSerialVersionUID = 1L;
    private UUID id;
    private final Instant createdAt;
    private final UUID typeId;
    private final String originalFilename;
    private final String contentType;
    private final byte[] bytes;

    public BinaryContent(UUID typeId, String originalFilename, String contentType, byte[] bytes) {
        this.id = (id != null) ? id : UUID.randomUUID();
        this.createdAt = Instant.now();
        this.typeId = typeId;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
