package com.sprint.mission.discodeit.dto.binaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileName,
        Long size,
        String contentType,
        byte[] bytes,
        Instant createdAt
) {
}
