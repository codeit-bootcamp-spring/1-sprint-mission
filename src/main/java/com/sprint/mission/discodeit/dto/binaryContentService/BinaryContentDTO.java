package com.sprint.mission.discodeit.dto.binaryContentService;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record BinaryContentDTO(
        UUID id,         // 파일 ID
        String fileName, // 파일 이름
        String mimeType  // 파일 타입 (예: image/png)
) {
    public static BinaryContentDTO from(BinaryContent binaryContent) {
        return new BinaryContentDTO(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getContentType()
        );
    }
}
