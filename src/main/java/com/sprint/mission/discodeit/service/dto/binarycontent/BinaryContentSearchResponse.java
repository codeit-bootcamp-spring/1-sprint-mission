package com.sprint.mission.discodeit.service.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentSearchResponse(
    UUID binaryContentId,
    Instant createAt,
    Instant updateAt
) {

    public static BinaryContentSearchResponse from(BinaryContent binaryContent) {
        return new BinaryContentSearchResponse(
            binaryContent.id(),
            binaryContent.createAt(),
            binaryContent.updateAt()
        );
    }
}
