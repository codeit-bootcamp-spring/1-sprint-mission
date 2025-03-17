package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
    UUID id,
    String content,
    Instant createdAt,
    Instant updatedAt,
    UUID channelId,
    UUID author,
    List<UUID> attachments
) {}