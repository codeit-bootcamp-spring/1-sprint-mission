package com.sprint.mission.discodeit.dto.message.response;

import java.util.UUID;

public record CreateMessageResponse(
        UUID messageId,
        String content
) {
}
