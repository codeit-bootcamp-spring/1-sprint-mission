package com.spirnt.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
        UUID userId,
        UUID channelId,
        Instant lastReadAT
) {
}
