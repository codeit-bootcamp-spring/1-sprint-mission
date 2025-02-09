package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;


public class ReadStatus {

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();
    }

    public void updateReadStatus(Instant time) {
        lastReadAt = time;
        setUpdatedAt();
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public Instant getLastReadAt() {
        return lastReadAt;
    }


    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }


}