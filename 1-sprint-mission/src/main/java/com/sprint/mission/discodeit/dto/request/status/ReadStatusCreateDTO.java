package com.sprint.mission.discodeit.dto.request.status;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateDTO(
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
