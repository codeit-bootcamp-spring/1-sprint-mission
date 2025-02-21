package com.sprint.mission.discodeit.dto.user_status;

import java.time.Instant;


public record UpdateUserStatusDto(
    Instant newLastActiveAt
) {
}
