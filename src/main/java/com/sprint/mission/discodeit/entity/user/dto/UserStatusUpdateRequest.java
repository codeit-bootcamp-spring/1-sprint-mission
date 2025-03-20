package com.sprint.mission.discodeit.entity.user.dto;

import java.time.Instant;

public record UserStatusUpdateRequest(
    Instant newLastActiveAt
) {

}
