package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public record BinaryContentDTO(
        UUID binaryContentId,
        String contentType,
        byte[] bytes

) {
    public static BinaryContentDTO fromEntity(BinaryContent binaryContent) {

        return new BinaryContentDTO(
                binaryContent.getId(),
                binaryContent.getContentType(),
                binaryContent.getBytes()
        );
    }
}
