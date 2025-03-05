package com.sprint.mission.discodeit.dto.user.userStatus;

import java.time.Instant;

public record UserStatusUpdateRequest(
    Instant newLastActiveAt
) {

}
