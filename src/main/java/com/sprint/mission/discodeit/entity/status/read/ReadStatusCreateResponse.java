package com.sprint.mission.discodeit.entity.status.read;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
