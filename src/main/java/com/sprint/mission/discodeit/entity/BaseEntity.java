package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class BaseEntity {
    private final String id;
    private final long createdAt;
    private long updatedAt;

    // 기본 생성자: id와 createdAt 초기화
    public BaseEntity() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public String getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
