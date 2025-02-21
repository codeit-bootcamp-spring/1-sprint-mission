package com.sprint.mission.discodeit.dto.message;

import java.time.Instant;
import java.util.List;

public record MessageResponseDto(
    String id, // messageId
    Instant createdAt,
    Instant updatedAt,
    String content,
    String channelId,
    String authorId,
    List<String> attachmentIds
) {
}
