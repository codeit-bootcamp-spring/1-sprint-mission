package com.sprint.mission.discodeit.dto.readStatus;

import io.swagger.v3.oas.models.security.SecurityScheme.In;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
        UUID channelId,
        UUID userId,
        Instant lastReadAt
) {
    public static ReadStatusCreateRequest from(UUID channelId, UUID userId, Instant lastReadAt) {
        return new ReadStatusCreateRequest(channelId, userId, lastReadAt);
    }
}
