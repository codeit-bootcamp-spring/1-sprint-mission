package com.sprint.mission.discodeit.entity.user;

import java.util.UUID;

public record UserCreateWithBinaryContentResponse(
        UUID userId,
        UUID messageId,
        String fileName,
        String fileType
) {
}
