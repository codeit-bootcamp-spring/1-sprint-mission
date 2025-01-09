package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class BaseEntity {
    private UUID id;
    private long createdAt;
    private long updatedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void update(){
        this.updatedAt = System.currentTimeMillis();
    }
}
