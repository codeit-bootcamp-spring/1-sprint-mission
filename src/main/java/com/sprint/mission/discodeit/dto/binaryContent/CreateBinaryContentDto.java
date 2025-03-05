package com.sprint.mission.discodeit.dto.binaryContent;

import java.time.Instant;

public record CreateBinaryContentDto(
        String filename,
        byte[] binaryImage,
        Instant createdAt
) {

}
