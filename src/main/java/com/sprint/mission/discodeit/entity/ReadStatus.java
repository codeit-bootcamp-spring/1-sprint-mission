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

    private Instant updatedAt;

    private final UUID channelId;
    private final UUID userId;

    public ReadStatus(UUID channelId, UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.channelId = channelId;
        this.userId = userId;
    }

    public boolean isSameChannelId(UUID channelId) {
        return this.channelId.equals(channelId);
    }

    public boolean isSameUserId(UUID userId) {
        return this.userId.equals(userId);
    }
}
