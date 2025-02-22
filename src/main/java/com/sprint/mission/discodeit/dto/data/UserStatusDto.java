package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        UUID userId,
        Instant lastActiveAt,
        Boolean online
) {
}
