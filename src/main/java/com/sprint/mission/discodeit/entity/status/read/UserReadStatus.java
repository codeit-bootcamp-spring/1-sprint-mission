package com.sprint.mission.discodeit.entity.status.read;

import java.time.Instant;

public record UserReadStatus(
        String userName,
        Instant lastReadAt
) {
}
