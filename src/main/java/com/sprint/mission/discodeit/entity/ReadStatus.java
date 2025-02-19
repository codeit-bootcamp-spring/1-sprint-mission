package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updateAt;

    private UUID userId;
    private UUID channelId;

    private ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updateAt = createdAt;
        this.userId = userId;
        this.channelId = channelId;
    }

    public static ReadStatus of(UUID userId, UUID channelId) {
        return new ReadStatus(userId, channelId);
    }

    public void update() {
        this.updateAt = Instant.now();
    }
}
