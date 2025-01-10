package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String name;
    private UUID ownerId;

    public Channel(String name, UUID ownerId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.name = name;
        this.ownerId = ownerId;
    }

    public UUID getId() { return id; }
    public long getCreatedAt() { return createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public String getName() { return name; }
    public UUID getOwnerId() { return ownerId; }

    public void updateName(String newName) {
        this.name = newName;
        this.updatedAt = System.currentTimeMillis();
    }
}
