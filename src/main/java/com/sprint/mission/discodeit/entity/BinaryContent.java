package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final UUID userId;
    private final UUID messageId;
    private final byte[] data;
    private final Instant creatAt;

    public BinaryContent(UUID userId, UUID messageId, byte[] data) {
        this.messageId = messageId;
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.data = data;
        this.creatAt = getCurrentTime();
    }
    protected Instant getCurrentTime() {
        return Instant.now();
    }
}
