package com.sprint.mission.discodeit.dto.userStatusDto;

import java.time.Instant;

public record UpdateUserStatusRequest(
    Instant newLastActiveAt
) {

}
