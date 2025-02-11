package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class UserStatus {
    private UUID id;
    private UUID userId;  // 사용자 ID
    private Instant lastActiveAt;  // 마지막 접속 시간
    private Instant createdAt;

    public boolean isCurrentlyActive() {
        // 5분 이내에 접속했으면 현재 접속 중인 유저로 간주
        return lastActiveAt != null && lastActiveAt.isAfter(Instant.now());
    }
}
