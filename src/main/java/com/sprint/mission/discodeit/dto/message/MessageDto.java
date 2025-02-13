package com.sprint.mission.discodeit.dto.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID messageId,
        UUID authorId,
        UUID channelId,
        String message,
        List<UUID> attachmentIds,

        Instant createdAt,
        Instant updatedAt
) {
}
