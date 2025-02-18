package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "user_status")
@NoArgsConstructor
public class UserStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID userId;  // 사용자 ID
    private Instant lastActiveAt;  // 마지막 접속 시간
    private Instant createdAt;
    private boolean isActive;

    public boolean isCurrentlyActive() {
        // 5분 이내에 접속했으면 현재 접속 중인 유저로 간주
        return lastActiveAt != null && lastActiveAt.isAfter(Instant.now());
    }

    public UserStatus(UUID userId, Instant lastActiveAt, boolean isActive) {
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
        this.isActive = isActive;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public void setIsActive(boolean isActive) {  // ✅ 직접 추가
        this.isActive = isActive;
    }

}
