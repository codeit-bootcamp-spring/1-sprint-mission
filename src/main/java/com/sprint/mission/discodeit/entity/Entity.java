package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Entity {
    private final UUID id ;
    private final long createdAt;
    private long updatedAt;

    //생성자
    public Entity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    //getter method
    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    //필드 수정 -> update 메소드
    public void update(long updatedAt) {
        this.updatedAt = System.currentTimeMillis();
    }
}
