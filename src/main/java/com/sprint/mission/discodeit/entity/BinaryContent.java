package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;

    private final byte[] data;

    public BinaryContent(byte[] data) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.data = data;
    }

    public boolean containsId(List<UUID> ids) {
        return ids.contains(this.id);
    }
}
