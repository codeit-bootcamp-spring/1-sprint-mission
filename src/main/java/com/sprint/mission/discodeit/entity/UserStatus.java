package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * - 사용자 별 마지막으로 확인된 접속 시간을 표현
 * - 사용자의 온라인 상태를 확인하기 위함
 */

@Getter
public class UserStatus implements Serializable {
    private UUID id;
    private UUID userId;
    private Instant accessedAt; // 접속 시간
    private Instant createdAt;
    private Instant updatedAt;  // 엔티티가 변경되었을 때

    public UserStatus(UUID userId, Instant accessedAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.accessedAt = accessedAt;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void updateAccessedAt() {
        this.accessedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public boolean isOnline() {
        return Duration.between(Instant.now(), accessedAt).toMinutes() <= 5L;
    }

    @Override
    public String toString() {
        return "UserStatus{" +
                "id=" + id +
                ", userId=" + userId +
                ", accessedAt=" + accessedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
