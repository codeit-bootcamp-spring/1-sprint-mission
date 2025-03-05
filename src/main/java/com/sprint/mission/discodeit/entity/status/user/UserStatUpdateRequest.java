package com.sprint.mission.discodeit.entity.status.user;

import java.time.Instant;

public record UserStatUpdateRequest(
    Instant newLastActiveAt
) {

}
