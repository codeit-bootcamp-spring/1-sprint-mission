package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileNm,
        String contentType,
        byte[] content,
        Long size,
        Instant createdAt
) {
    public static BinaryContentDto from(BinaryContent content) {
        return new BinaryContentDto(
                content.getId(),
                content.getFileNm(),
                content.getContentType(),
                content.getContent(),
                content.getSize(),
                content.getCreatedAt()
        );
    }
}
