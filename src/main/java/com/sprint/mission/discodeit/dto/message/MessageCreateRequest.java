package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageCreateRequest(
        String content,
        UUID authorId,
        UUID channelId
) {
}
