package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class ReadStatus {

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private UUID channelId;

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.channelId = channelId;
    }



    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}