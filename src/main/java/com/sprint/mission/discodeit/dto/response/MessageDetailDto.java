package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.UUID;

public record MessageDetailDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String content,
        UUID writer,
        UUID channelId
) {
    public static MessageDetailDto from(Message message) {
        return new MessageDetailDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getWriter().getId(),
                message.getChannel().getId()
        );
    }
}
