package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

  public Message toEntity(User sender, Channel channel, String content) {
    return Message.of(sender, channel, content);
  }

  public MessageDto toMessageDto(Message message, List<UUID> attachmentIds) {
    return new MessageDto(message.getId(), message.getCreatedAt(), message.getUpdatedAt(),
        message.getContent(), message.getChannel().getId(), message.getSender().getId(),
        attachmentIds);
  }
}
