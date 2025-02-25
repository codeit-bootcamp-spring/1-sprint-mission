package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record MessageRequest() {
    public record Create(
            String content,
            UUID channelId,
            UUID userId
    ) {}

    public record Update(
            String content
    ) {}
}
