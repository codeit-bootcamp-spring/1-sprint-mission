package com.sprint.mission.discodeit.entity.status.read;

import java.time.Instant;

public record ReadStatusModifyRequest(
    Instant newLastReadAt
) {

}
