package com.sprint.mission.discodeit.dto.readstatus;

import java.time.Instant;

public record ReadStatusResponseDto(
    String id,
    Instant createdAt,
    Instant updatedAt,
    String userId,
    String channelId,
    Instant lastReadAt
) {
}
