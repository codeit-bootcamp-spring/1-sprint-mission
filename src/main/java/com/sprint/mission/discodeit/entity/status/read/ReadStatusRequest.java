package com.sprint.mission.discodeit.entity.status.read;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusRequest(
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
