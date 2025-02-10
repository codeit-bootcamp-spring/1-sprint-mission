package com.sprint.mission.discodeit.application.dto.message;

import com.sprint.mission.discodeit.domain.message.Message;
import java.util.UUID;

public record MessageResponseDto(
        UUID messageId,
        String content,
        String senderName,
        UUID destinationChannelId
) {
    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getSenderName(),
                message.getContent(),
                message.getDestinationChannelId());
    }
}
