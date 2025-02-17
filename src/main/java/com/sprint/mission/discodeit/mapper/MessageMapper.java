package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageDto messageEntityToDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getAuthorId(),
                message.getChannelId(),
                message.getMessage(),
                message.getAttachmentIds(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
