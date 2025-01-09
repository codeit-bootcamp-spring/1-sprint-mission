package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public abstract class CommonEntity {
    // UUID ID
    private UUID id;
    // 생성시간
    private long createdAt;
    // 갱신시간
    private long updatedAt;

    public CommonEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void updateId(UUID id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void updateCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void updateUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
