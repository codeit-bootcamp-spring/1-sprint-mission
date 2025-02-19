package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID userId;
    private UUID channelId;

    public static ReadStatus createReadStatus(UUID userId, UUID channelId) {
        return new ReadStatus(userId, channelId);
    }

    private ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.userId = userId;
        this.channelId = channelId;
    }

    public void updateUpdateAt() {
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "ReadStatus{id:" + id
                + ",userId:" + userId
                + ",channelId:" + channelId
                + ",createdAt:" + createdAt
                + ",updateAt:" + updatedAt
                + "}";
    }
}
