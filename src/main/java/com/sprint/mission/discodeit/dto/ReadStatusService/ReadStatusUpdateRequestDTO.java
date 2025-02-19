package com.sprint.mission.discodeit.dto.ReadStatusService;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateRequestDTO(
        UUID readStatusId,
        Instant lastReadAt
) {
}
