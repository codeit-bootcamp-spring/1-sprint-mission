package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.UUID;

public record MessageDetailResponse(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String content,
        UUID writer,
        UUID channelId
) {
    public static MessageDetailResponse from(Message message) {
        return new MessageDetailResponse(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getWriter().getId(),
                message.getChannel().getId()
        );
    }
}
