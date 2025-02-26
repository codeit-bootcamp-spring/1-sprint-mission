package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageCreateDto(
        String content,
        UUID userId,
        UUID channelId
) {
}
