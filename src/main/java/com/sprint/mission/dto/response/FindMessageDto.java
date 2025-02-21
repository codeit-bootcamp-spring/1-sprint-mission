package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.Message;
import java.util.UUID;

import java.util.List;



public record FindMessageDto (UUID writerId, UUID channelId, List<UUID> attachmentIdList, String content) {
    public static FindMessageDto fromEntity(Message message) {
        return new FindMessageDto(
            message.getWriterId(),
            message.getChannelId(),
            message.getAttachmentIdList(),
            message.getContent());
    }
}
