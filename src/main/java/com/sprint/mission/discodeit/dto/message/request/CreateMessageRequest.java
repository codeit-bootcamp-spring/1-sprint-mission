package com.sprint.mission.discodeit.dto.message.request;

import java.util.UUID;

public record CreateMessageRequest(
        UUID senderId,
        UUID channelId,
        String content
) {
}
