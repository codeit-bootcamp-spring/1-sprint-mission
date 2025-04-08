package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;

public record UserStatusUpdateRequest(
        Instant newLastActiveAt
) {
}
