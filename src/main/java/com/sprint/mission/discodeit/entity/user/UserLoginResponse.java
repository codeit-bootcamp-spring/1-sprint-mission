package com.sprint.mission.discodeit.entity.user;

import java.time.Instant;
import java.util.UUID;

public record UserLoginResponse(
        UUID userId,
        String userName,
        Instant loginAt
) {
}
