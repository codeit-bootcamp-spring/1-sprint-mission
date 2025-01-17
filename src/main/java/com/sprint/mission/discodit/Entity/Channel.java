package com.sprint.mission.discodit.Entity;

import java.util.UUID;

public class Channel {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String name;

    public Channel(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.name = name;
    }
    public long setUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
        return updatedAt;
    }

    public void update(String name) {
        this.name = name;
        updatedAt = setUpdatedAt();
    }

    public UUID getChannel() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Channel{" + "id=" + id + '}';
    }
}
