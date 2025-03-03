package com.sprint.mission.discodeit.dto.readStatusDto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
