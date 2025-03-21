package com.sprint.mission.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
        UUID id,
        UUID userId,
        Instant lastActivityAt) {
}
