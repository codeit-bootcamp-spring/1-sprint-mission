package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageUpdateRequestDto(
        UUID id,
        String content
) {
}
