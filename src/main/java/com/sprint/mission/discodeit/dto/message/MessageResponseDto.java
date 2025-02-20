package com.sprint.mission.discodeit.dto.message;

import java.time.Instant;
import java.util.List;

public record MessageResponseDto(
    String messageId,
    String userId,
    String channelId,
    String content,
    Instant createdAt,
    List<String> base64Data
) {
}
