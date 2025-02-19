package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

// UserStatus: 사용자의 마지막 접속 시간 관리 및 온라인 상태 확인
public class UserStatus extends BaseEntity {
    private UUID userId;
    private Instant lastSeenAt;

    // 현재 접속 상태 여부 (5분 이내 접속)
    public boolean isOnline() {
        return lastSeenAt != null && Instant.now().minusSeconds(300).isBefore(lastSeenAt);
    }
}
