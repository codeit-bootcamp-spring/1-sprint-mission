package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponse(
        UUID id,
        String fileNm,
        String contentType,
        Long size,
        Instant createdAt
) {
    public static BinaryContentResponse from(BinaryContent content) {
        return new BinaryContentResponse(
                content.getId(),
                content.getFileNm(),
                content.getContentType(),
                content.getSize(),
                content.getCreatedAt()
        );
    }
}