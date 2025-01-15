package com.sprint.mission.discodeit.entity;

// 직렬화 역직렬화를 할 때 id도 고정된 데이터로 유지하도록 User 클래스만 하는 것이 아닌 BaseEntity도 해줘야한다.

import java.util.UUID;
import java.io.Serializable;
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    // 생성자
    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    public BaseEntity(UUID id, long createdAt, long updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    // 수정 시간 갱신
    public void update() {
        this.updatedAt = System.currentTimeMillis();
    }
}
