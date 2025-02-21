package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
        UUID userId,
        UUID channelId,
        String message,
        List<byte[]> content
) {}