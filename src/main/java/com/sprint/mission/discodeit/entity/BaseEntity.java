package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class BaseEntity {
    private final UUID id ;
    private final Long createdAt;
    private Long updatedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
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

    //update 메소드
    public void update() {
        this.updatedAt = System.currentTimeMillis();
    }
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
