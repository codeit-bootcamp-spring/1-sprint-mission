package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;

public record ResponseBinaryContentDto(
        String id,
        byte[] bytes,
        String ContentType,
        Instant createdAt
) {
    public static ResponseBinaryContentDto from(BinaryContent binaryContent) {
        return new ResponseBinaryContentDto(
                binaryContent.getId(),
                binaryContent.getBinaryImage(),
                binaryContent.getContentType(),
                binaryContent.getCreatedAt()
        );
    }
}
