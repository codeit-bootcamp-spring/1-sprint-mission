package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
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
        this.updatedAt = System.currentTimeMillis();
    }
}
