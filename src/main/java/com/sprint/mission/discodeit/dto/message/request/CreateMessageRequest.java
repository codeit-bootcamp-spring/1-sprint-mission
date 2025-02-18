package com.sprint.mission.discodeit.dto.message.request;

import java.util.UUID;

public record CreateMessageRequest(
        UUID sendUserId,
        UUID channelId,
        String content
) {
}
