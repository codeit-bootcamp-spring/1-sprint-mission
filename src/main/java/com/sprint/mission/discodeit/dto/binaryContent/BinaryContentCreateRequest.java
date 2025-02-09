package com.sprint.mission.discodeit.dto.binaryContent;

import java.util.UUID;

public record BinaryContentCreateRequest(
        UUID userId,
        UUID messageId,
        byte[] content
) {
}
