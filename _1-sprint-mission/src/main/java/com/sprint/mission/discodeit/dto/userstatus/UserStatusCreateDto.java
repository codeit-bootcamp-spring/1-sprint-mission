package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateDto(
        UUID userId,
        Instant lastConnetTime
) {
}
