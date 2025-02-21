package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
        User user,
        Instant lastConnetTime,
        boolean onelineStatus
) {
}
