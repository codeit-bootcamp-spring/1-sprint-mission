package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
        UUID userId,
        Instant lastActiveAt
) {
    public static UserStatusCreateRequest from(UUID userId, Instant lastActiveAt) {
        return new UserStatusCreateRequest(userId, lastActiveAt);
    }
}
