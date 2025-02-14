package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDTO(UUID userId, UUID channelId, Instant lastReadAt) {
}
