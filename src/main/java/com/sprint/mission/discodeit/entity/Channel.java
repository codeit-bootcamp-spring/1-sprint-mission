package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String name;
    private UUID ownerId;

    public Channel(String name, UUID ownerId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.name = name;
        this.ownerId = ownerId;
    }


    // 각 필드의 Getter 함수 정의
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // 현재는 사용하지 않는 값이지만, 확장성을 위해 모든 필드에 대해 Getter 함수 작성
    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void updateName(String newName) {
        this.name = newName;
        this.updatedAt = System.currentTimeMillis();
    }
}
