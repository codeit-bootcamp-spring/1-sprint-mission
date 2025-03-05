package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;

public record ReadStatusUpdateRequest(
    Instant newLastReadAt
) {

}
