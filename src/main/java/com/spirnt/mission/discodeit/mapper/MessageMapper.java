package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.message.MessageDto;
import com.spirnt.mission.discodeit.enity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BinaryContentMapper.class})
public interface MessageMapper {

  @Mapping(source = "channel.id", target = "channelId")
  MessageDto toDto(Message message);
}
