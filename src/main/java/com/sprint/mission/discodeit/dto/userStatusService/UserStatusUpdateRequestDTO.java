package com.sprint.mission.discodeit.dto.userStatusService;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequestDTO(
        UUID userStatusId,
        Instant lastActive
) {
}
