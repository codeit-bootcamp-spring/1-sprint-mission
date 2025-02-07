package com.sprint.mission.discodeit.dto.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID messageId,
        UUID userId,
        UUID channelId,
        String message,
        List<byte[]> content,
        Instant createdAt,
        Instant updatedAt
) {
}
