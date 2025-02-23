package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageSendRequest(
    UUID channelId,
    UUID senderId,
    UUID replyToId,
    List<UUID> binaryContentIds,
    String content
) {
}
