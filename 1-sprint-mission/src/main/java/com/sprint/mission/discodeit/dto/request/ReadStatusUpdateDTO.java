package com.sprint.mission.discodeit.dto.request;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateDTO(
        UUID readStatusId,
        Instant lastReadAt
) {
}
