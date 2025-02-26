package com.sprint.mission.discodeit.dto.readstatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateDto(
        UUID userId,
        UUID channelId,
        Instant lastRead
) {
}
