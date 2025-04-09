package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    Instant createdAt,
    String fileName,
    long size,
    String contentType
) {

}
