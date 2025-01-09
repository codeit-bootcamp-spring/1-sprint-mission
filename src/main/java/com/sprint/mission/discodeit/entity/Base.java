package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Base {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    public Base() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void getUpdatedAt() {
       this.updatedAt=System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Base{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
