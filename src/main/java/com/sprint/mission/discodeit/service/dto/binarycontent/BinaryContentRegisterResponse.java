package com.sprint.mission.discodeit.service.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentRegisterResponse(
    UUID binaryContentId,
    Instant createAt,
    Instant updateAt
) {

    public static BinaryContentRegisterResponse from(BinaryContent binaryContent) {
        return new BinaryContentRegisterResponse(
            binaryContent.id(),
            binaryContent.createAt(),
            binaryContent.updateAt()
        );
    }
}
