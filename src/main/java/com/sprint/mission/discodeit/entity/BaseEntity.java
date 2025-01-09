package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class BaseEntity {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    // 기본 생성자: id와 createdAt 초기화
    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void updateTime() {
        this.updatedAt = updatedAt;
    }
    public void updateTime(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
