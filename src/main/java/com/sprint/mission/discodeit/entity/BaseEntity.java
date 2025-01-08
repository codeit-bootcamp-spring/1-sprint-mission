package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class BaseEntity {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    public BaseEntity(){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
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

    public void update(){
        this.updatedAt = System.currentTimeMillis();
    }
}
