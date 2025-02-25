package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreate(UUID userId, UUID channelId, Instant lastReadAt) {
}
