package com.sprint.mission.discodeit.dto.binary_content;

import java.time.Instant;

public record BinaryContentDto(
    String id,
    Instant createdAt,
    String fileName,
    int size,
    String contentType,
    String bytes
) {
}
