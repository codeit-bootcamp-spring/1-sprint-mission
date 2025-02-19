package com.sprint.mission.discodeit.dto.userStatusService;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequestDTO(
        UUID userId,
        Instant lastActive
) {
    public UserStatus from() {
        return new UserStatus(
                userId,
                lastActive != null ? lastActive : Instant.now() // 기본값 설정
        );
    }
}
