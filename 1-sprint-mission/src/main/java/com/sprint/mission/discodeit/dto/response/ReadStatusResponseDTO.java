package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponseDTO(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {}
