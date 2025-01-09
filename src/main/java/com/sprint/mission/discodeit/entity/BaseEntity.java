package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class BaseEntity {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    // 생성자
    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    // Getter 함수
    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    // update 메서드
    public void update() {
        this.updatedAt = System.currentTimeMillis(); // 수정 시간 갱신
    }
}