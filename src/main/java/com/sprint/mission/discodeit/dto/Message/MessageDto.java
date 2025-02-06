package com.sprint.mission.discodeit.dto.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID id,
        String content,
        Instant createdAt,
        List<UUID> attachments
) {
}
