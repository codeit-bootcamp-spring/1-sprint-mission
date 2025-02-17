package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final UUID userId;
    private final UUID messageId;
    private final String filename;
    private final String contentType;
    private final byte[] fileData;
    private final Instant createdAt;

    public BinaryContent(UUID id, UUID userId, UUID messageId ,String filename, String contentType, byte[] fileData) {
        this.id = (id != null) ? id : UUID.randomUUID();
        this.userId = userId;
        this.messageId = messageId;
        this.filename = filename;
        this.contentType = contentType;
        this.fileData = fileData;
        this.createdAt = Instant.now();
    }
}
