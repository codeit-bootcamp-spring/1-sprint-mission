package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.Base64;
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
