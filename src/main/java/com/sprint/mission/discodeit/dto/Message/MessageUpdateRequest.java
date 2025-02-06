package com.sprint.mission.discodeit.dto.Message;

import java.util.List;
import java.util.UUID;

public record MessageUpdateRequest(
        UUID messageId,
        String newContent,
        List<UUID> attachmentIds
) {}
