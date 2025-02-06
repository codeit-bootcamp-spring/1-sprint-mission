package com.sprint.mission.discodeit.dto;

import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(UUID id, UUID userId, UUID channelId, Instant lastReadAt, Instant createdAt, Instant updatedAt) {
}
