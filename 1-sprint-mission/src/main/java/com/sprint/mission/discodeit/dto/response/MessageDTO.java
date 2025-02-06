package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.UUID;

public record MessageDTO(UUID id, UUID userId, UUID channelId, String content, Instant createdAt, Instant updatedAt) {
}
