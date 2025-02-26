package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageFindBResponse(
        UUID id,
        UUID channelId,
        UUID authorId,
        String messageText
) {
}
