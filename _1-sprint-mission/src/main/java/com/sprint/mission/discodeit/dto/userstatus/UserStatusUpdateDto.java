package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;

public record UserStatusUpdateDto(
        Instant newLastConnectTime
) {
}
