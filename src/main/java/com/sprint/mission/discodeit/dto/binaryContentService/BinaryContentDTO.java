package com.sprint.mission.discodeit.dto.binaryContentService;

import java.util.UUID;

public record BinaryContentDTO(
        UUID id,         // 파일 ID
        String fileName, // 파일 이름
        String mimeType  // 파일 타입 (예: image/png)
) {
}
