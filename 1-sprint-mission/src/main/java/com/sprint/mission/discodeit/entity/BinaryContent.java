package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private final UUID id;
    private final User user;
    private final String filename;
    private final String contentType;
    private final byte[] fileData;
    private final Instant createdAt;

    public BinaryContent(UUID id, User user, String filename, String contentType, byte[] fileData) {
        this.id = (id != null) ? id : UUID.randomUUID();
        this.user = user;
        this.filename = filename;
        this.contentType = contentType;
        this.fileData = fileData;
        this.createdAt = Instant.now();
    }
}
