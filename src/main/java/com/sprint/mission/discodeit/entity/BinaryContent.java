package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent {
    private final UUID id;
    private final Instant createdAt;

    private final OwnerType type;
    private final UUID ownerId;
    private final String originalName;
    private final byte[] data;

    private BinaryContent(OwnerType type, UUID ownId, String originalName, byte[] data) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.type = type;
        this.ownerId = ownId;
        this.originalName = originalName;
        this.data = data;
    }

    public static BinaryContent of(OwnerType type, UUID ownId, String originalName, byte[] data) {
        return new BinaryContent(type, ownId, originalName, data);
    }
}
