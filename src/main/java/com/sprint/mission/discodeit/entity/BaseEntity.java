package com.sprint.mission.discodeit.entity;

import java.util.UUID;

// 공통 필드를 포함하는 기본 엔티티 클래스
public abstract class BaseEntity {
    private final UUID id; // 각 객체의 고유 식별자
    private final long createdAt; // 객체 생성 시간
    private long updatedAt; // 객체 수정 시간

    // 생성자를 통해 id와 타임스탬프를 초기화
    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    // id를 반환
    public UUID getId() {
        return id;
    }

    // 생성 시간을 반환
    public long getCreatedAt() {
        return createdAt;
    }

    // 수정 시간을 반환
    public long getUpdatedAt() {
        return updatedAt;
    }

    // 수정 시간을 현재 시간으로 업데이트
    public void updateTimestamp() {
        this.updatedAt = System.currentTimeMillis();
    }
}