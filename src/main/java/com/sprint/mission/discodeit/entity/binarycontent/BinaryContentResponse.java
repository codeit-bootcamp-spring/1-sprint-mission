package com.sprint.mission.discodeit.entity.binarycontent;

import java.util.UUID;

public record BinaryContentResponse(
        UUID userId,
        UUID messageId,
        String fileName,
        String fileType
) {
}
