package com.sprint.mission.discodeit.dto.readStatusDto;

import java.time.Instant;

public record ReadStatusUpdateRequest(
    Instant newLastReadAt
) {

}
