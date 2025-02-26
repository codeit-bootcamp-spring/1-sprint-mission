package com.spirnt.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageCreateResponse(
        UUID messageId,
        String content
) {
}
