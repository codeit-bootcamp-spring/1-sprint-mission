package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public abstract class   BaseEntity {
    private final UUID id; // UUID로 고유 식별자
    private final Long createdAt; // 생성시간
    private long updateAT; // 수정시간

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updateAT = this.createdAt;
    }

    public UUID getId() {
        return id;
    }
    public Long getCreatedAt() {
        return createdAt;
    }
    public long getUpdateAT() {
        return updateAT;
    }
    public void setUpdateAT(long updateAT) {
        this.updateAT = updateAT;
    }
}
