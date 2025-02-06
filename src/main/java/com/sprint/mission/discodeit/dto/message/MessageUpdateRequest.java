package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageUpdateRequest(
        UUID messageId,
        UUID userId,
        String newMessage,
        List<UUID> deletedContentIds,
        List<byte[]> newContent
) {}
