package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID userId,
        String username,
        String email,
        String phoneNumber,
        boolean isOnline,
        Instant createdAt,
        Instant updatedAt
) {
}
