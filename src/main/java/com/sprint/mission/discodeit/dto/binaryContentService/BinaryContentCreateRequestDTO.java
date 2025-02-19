package com.sprint.mission.discodeit.dto.binaryContentService;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record BinaryContentCreateRequestDTO(
        UUID userId,
        UUID messageId,
        String mimeType,
        String fileName,
        byte[] data
) {
    public BinaryContent from() {
        return new BinaryContent(
                userId,
                messageId,
                mimeType,
                fileName,
                data
        );
    }
}
