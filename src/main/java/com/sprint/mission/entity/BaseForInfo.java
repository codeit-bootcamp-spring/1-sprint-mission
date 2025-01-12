package com.sprint.mission.entity;

import java.util.UUID;

public class BaseForInfo {
    protected UUID UserId;
    private final long createdAt;
    private long updateAt;

    //UUID 타입
    public BaseForInfo(UUID UserId, int createdAt, int updateAt){
        this.UserId = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updateAt = this.createdAt;
    }

    public UUID getUserId() {
        return UserId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUserId(UUID userId) {
        UserId = userId;
    }
}
