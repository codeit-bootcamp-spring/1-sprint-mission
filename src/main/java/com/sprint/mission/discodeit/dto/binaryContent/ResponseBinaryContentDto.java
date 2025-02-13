package com.sprint.mission.discodeit.dto.binaryContent;

import java.time.Instant;

public record ResponseBinaryContentDto(
        String id,
        byte[] binaryImage,
        Instant createdAt
) {
}
