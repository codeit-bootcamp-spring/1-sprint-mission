package com.sprint.mission.discodeit.entity;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ReadStatus{
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;

        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();
    }
    public void updateRead(Instant newLastReadAt) {
        this.lastReadAt = newLastReadAt;
        this.updatedAt = Instant.now();
    }
}
