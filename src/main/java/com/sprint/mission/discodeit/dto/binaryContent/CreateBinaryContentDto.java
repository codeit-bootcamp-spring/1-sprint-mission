package com.sprint.mission.discodeit.dto.binaryContent;

import java.time.Instant;

public record CreateBinaryContentDto(
        byte[] binaryImage,
        Instant createdAt
) {
}
