package com.sprint.mission.discodeit.entity.user.dto;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateResponse(
    UUID id,
    UUID userId,
    Instant lastActiveAt
) {

}
