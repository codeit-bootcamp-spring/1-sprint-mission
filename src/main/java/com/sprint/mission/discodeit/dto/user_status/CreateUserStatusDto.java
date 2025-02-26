package com.sprint.mission.discodeit.dto.user_status;

import java.time.Instant;

public record CreateUserStatusDto(
    String userId,
    Instant lastOnlineAt
) {
}
