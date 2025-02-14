package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponseDTO(
        UUID id,
        UUID userId,
        String filename,
        String contentType,
        Instant createdAt
) {
    public static BinaryContentResponseDTO fromEntity(BinaryContent binaryContent) {
        return new BinaryContentResponseDTO(
                binaryContent.getId(),
                binaryContent.getUserId(),
                binaryContent.getFilename(),
                binaryContent.getContentType(),
                binaryContent.getCreatedAt()
        );
    }
}
