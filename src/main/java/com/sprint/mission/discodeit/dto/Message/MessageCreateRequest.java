package com.sprint.mission.discodeit.dto.Message;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
        UUID userId,
        String content,
        List<UUID> attachmentIds
) {}