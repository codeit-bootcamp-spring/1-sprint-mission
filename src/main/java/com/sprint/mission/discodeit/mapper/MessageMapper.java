package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class})
public interface MessageMapper {

  MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

  default UUID map(Message message) {
    return message.getId();
  }

  default UUID map(Channel channel) {
    return channel.getId();
  }

  @Mapping(source = "channel", target = "channelId")
  @Mapping(source = "author", target = "author")
  MessageResponse toMessageResponse(Message message);
}
