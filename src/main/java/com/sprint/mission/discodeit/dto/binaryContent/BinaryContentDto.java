package com.sprint.mission.discodeit.dto.binaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        UUID userId,
        UUID messageId,
        Instant createdAt,
        byte[] content
)
{ }
