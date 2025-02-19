package com.sprint.mission.discodeit.entity.binarycontent;

import java.util.UUID;

public record BinaryContentRequest(
        UUID userId,
        UUID messageId,
        String fileName,
        String fileType
) {
}
