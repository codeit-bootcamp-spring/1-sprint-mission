package com.sprint.mission.discodeit.entity.message;

import java.util.UUID;

public record MessageResponse (
        UUID messageId,
        String title,
        String content,
        UUID senderId,
        UUID receiverId
) {
}
