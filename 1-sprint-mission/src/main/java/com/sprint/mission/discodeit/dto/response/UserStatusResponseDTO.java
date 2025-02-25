package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDTO(
        UUID statusId,
        UUID userId,
        Instant lastActiveAt
) {
}
