package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public abstract class BaseEntity {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis() / 1000L;
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void updateTimeStamp(){
        this.updatedAt = System.currentTimeMillis() / 1000L;
    }
}