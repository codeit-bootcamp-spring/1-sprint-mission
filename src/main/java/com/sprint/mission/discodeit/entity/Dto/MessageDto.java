package com.sprint.mission.discodeit.entity.Dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record MessageDto(
        Instant createdAt,
        Instant updatedAt,
        UUID id,
        User fromUser,
        Channel channel,
        String content
) {
    public static MessageDto fromDto(Message message) {
        return new MessageDto(message.getCreatedAt(), message.getUpdatedAt(), message.getId(), message.getAuthor(), message.getChannel(), message.getContent());
    }
}
