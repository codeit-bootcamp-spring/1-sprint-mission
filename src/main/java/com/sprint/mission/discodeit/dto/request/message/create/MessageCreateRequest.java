package com.sprint.mission.discodeit.dto.request.message.create;

import java.util.UUID;

public record MessageCreateRequest(
        String content,
        UUID channelId,
        UUID authorId
) {
}
