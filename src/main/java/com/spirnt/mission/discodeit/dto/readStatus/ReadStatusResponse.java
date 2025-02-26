package com.spirnt.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(
        UUID readStatusId,
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {
}
