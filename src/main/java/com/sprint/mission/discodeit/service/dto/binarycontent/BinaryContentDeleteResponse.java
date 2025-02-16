package com.sprint.mission.discodeit.service.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentDeleteResponse(
    UUID binaryContentId,
    Instant createAt,
    Instant updateAt
) {

    public static BinaryContentDeleteResponse from(BinaryContent binaryContent) {
        return new BinaryContentDeleteResponse(
            binaryContent.id(),
            binaryContent.createAt(),
            binaryContent.updateAt()
        );
    }
}
