package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequest(
        Instant newLastActiveAt
) {}

