package com.sprint.mission.discodeit.dto.binaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
        UUID contentId,
        UUID userId,
        UUID messageId,
        Instant createdAt,
        byte[] content
)
{ }
