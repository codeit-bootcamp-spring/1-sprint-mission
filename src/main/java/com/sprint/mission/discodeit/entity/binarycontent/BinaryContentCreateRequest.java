package com.sprint.mission.discodeit.entity.binarycontent;

import java.util.UUID;

public record BinaryContentCreateRequest(
        UUID userId,
        UUID messageId,
        String fileName,
        String fileType
) {
}
