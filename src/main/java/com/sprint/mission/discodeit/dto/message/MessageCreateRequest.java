package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
        UUID authorId,
        UUID channelId,
        String message
) {}