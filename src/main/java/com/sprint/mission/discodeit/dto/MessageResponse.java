package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public record MessageResponse(
        String content,
        UUID senderId,
        UUID recipientId,
        UUID channelId
) {
    public static MessageResponse fromEntity(Message message){
        return new MessageResponse(
                message.getContent(),
                message.getSenderId(),
                message.getRecipientId(),
                message.getChannelId()
        );
    }
}
