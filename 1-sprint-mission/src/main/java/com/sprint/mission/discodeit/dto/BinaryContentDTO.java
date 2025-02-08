package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDTO(
        UUID id,
        UUID userId,
        String filename,
        String contentType,
        Instant createdAt
) {
    public static BinaryContentDTO fromEntity(BinaryContent binaryContent) {
        return new BinaryContentDTO(
                binaryContent.getId(),
                binaryContent.getUser().getId(),
                binaryContent.getFilename(),
                binaryContent.getContentType(),
                binaryContent.getCreatedAt()
        );
    }
}
