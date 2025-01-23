package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String name;
    private UUID ownerId;

    public Channel(String name, UUID ownerId) {
        // 유효성 검사 추가
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("채널 이름이 비어 있습니다.");
        }
        if (ownerId == null) {
            throw new IllegalArgumentException("소유자 ID가 비어 있습니다..");
        }

        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.name = name;
        this.ownerId = ownerId;
    }

    // 각 필드의 Getter 함수 정의
    public UUID getId() { return id; }
    public String getName() { return name; }
    public long getCreatedAt() { return createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public UUID getOwnerId() { return ownerId; }

    public void updateName(String newName) {
        // 업데이트 시에도 유효성 검사 추가
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("새 채널 이름이 비어 있습니다.");
        }
        this.name = newName;
        this.updatedAt = System.currentTimeMillis();
    }
}
