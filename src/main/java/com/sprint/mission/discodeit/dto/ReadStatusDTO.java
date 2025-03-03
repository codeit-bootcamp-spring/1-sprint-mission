package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class ReadStatusDTO {
    public record request(
        UUID userId,
        UUID channelId,
        UUID messageId
    ){}

    public record response(
        UUID id,
        UUID userId,
        UUID channelId,
        UUID messageId,
        boolean isRead
    ){}
}
