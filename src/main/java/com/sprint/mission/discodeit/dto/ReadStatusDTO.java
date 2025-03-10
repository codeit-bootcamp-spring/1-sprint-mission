package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public class ReadStatusDTO {
    public record request(
        UUID userId,
        UUID channelId
    ){}

    public record response(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant lastRead_at
    ){}
}
