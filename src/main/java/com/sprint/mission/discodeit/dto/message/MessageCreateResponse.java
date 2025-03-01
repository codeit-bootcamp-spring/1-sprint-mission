package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageCreateResponse(
        UUID channelId,
        UUID authorId,
        String messageText,
        List<UUID> attachFiles
) {
}
