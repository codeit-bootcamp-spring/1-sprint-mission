package com.sprint.mission.dto.mappedDto;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
        UUID id,
        UUID userId,
        Instant lastActivityAt) {
}
