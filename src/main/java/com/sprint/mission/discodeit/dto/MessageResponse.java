package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID userId,
        UUID channelId,
        String content,
        Instant createdAt,
        Instant updatedAt
) {
    public static MessageResponse EntityToDto(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getUserId(),
                message.getChannelId(),
                message.getContent(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
