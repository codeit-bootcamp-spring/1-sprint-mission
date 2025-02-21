package com.sprint.mission.discodeit.dto.readstatus;

import java.time.Instant;
import java.util.UUID;

public record UpdateReadStatusDto(
        UUID userId,
        UUID channelId,
        Instant lastRead
) {
}
