package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        Instant createdAt,
        Instant updatedAt,
        UUID profileId,
        Boolean online
) {
}