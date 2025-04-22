package com.sprint.mission.discodeit.dto.readstatus;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusResponseDto(UUID id, UUID userId, UUID channelId, Instant lastReadAt) {
}
