package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

public record MessageDTO(
        UUID messageId,
        UUID channelId,
        UUID writerId,
        String content,
        List<UUID> attachmentIds
) {}
