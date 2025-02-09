package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class ReadStatus {

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private UUID channelId;


    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}