package com.sprint.mission.discodeit.entity.Dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;

import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        Instant createdAt,
        UUID uploaderId,
        BufferedImage image,
        BinaryContentType type
) {
    public static BinaryContentDto from (BinaryContent binaryContent){
        return new BinaryContentDto(binaryContent.getId(), binaryContent.getCreatedAt(), binaryContent.getUploaderId(), binaryContent.getImage(), binaryContent.getType());
    }
}
