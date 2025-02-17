package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDTO(
        UUID binaryId,
        UUID userId,
        UUID messageId,
        String filename,
        String contentType,
        byte[] fileData,
        Instant createdAt
) {
    public static BinaryContentDTO fromEntity(BinaryContent binaryContent) {
        return new BinaryContentDTO(
                binaryContent.getId(),
                binaryContent.getUserId(),
                binaryContent.getMessageId(),
                binaryContent.getFilename(),
                binaryContent.getContentType(),
                binaryContent.getFileData(),
                binaryContent.getCreatedAt()
        );
    }
}
