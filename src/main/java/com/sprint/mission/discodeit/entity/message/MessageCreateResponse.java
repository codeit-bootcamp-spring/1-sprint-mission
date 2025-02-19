package com.sprint.mission.discodeit.entity.message;

import java.util.UUID;

public record MessageCreateResponse(
        UUID messageId,
        String title,
        String content,
        UUID senderId,
        UUID receiverId
) {
}
