package com.sprint.mission.discodeit.dto.MessageService;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequestDTO(
        String content,
        UUID channelId,
        UUID authorId,
        List<UUID> fileIds
) {
}
