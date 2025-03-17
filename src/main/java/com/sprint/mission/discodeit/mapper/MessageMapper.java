package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserMapper.class})
public interface MessageMapper {

  @Mapping(target = "author", source = "sender")
  @Mapping(target = "channelId", expression = "java(message.getChannel().getId())")
  @Mapping(target = "attachments", source = "attachmentsList")
  MessageDto toMessageDto(Message message);
}
