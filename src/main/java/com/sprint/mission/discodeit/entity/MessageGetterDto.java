package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public record MessageGetterDto(
        Instant createdAt,
        Instant updatedAt,
        UUID id,
        User fromUser,
        Channel channel,
        String content
) {
    public static MessageGetterDto fromDto(Message message) {
        return new MessageGetterDto(message.getCreatedAt(), message.getUpdatedAt(), message.getId(), message.getAuthor(), message.getChannel(), message.getContent());
    }
}
