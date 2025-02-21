package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequest(
        UUID userStatusId,
        UUID userId,
        Instant lastLoginTime
) {}

