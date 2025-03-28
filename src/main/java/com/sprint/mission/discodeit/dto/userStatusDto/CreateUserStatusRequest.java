package com.sprint.mission.discodeit.dto.userStatusDto;

import java.time.Instant;
import java.util.UUID;

public record CreateUserStatusRequest(
    UUID userId,
    Instant lastActiveAt
) {

}
