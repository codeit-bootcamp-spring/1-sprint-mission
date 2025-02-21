package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record UpdateMessageDto(
        UUID messageId,
        String content
) {
}
