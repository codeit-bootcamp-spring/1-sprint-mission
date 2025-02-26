package com.sprint.mission.discodeit.entity.binarycontent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponse(
    UUID id,
    Instant createdAt,
    String fileName,
    Long size,
    String contentType,
    String bytes
) {

}
