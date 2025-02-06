package com.sprint.mission.discodeit.dto.binaryContent;

import java.util.UUID;

public record BinaryContentCreateRequest(
        UUID userId,
        UUID messageId,
        byte[] content
) {
    public BinaryContentCreateRequest {
        if (content == null || content.length == 0) {
            throw new IllegalArgumentException("Content cannot be empty.");
        }
    }
}
