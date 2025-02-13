package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
        UUID userStatusId,
        UUID userId,
        Instant lastActiveTime,

        Instant createdAt,
        Instant updatedAt
) {
}
