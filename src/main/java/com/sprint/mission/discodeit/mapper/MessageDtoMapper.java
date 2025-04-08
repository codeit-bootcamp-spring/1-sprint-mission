package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.entity.message.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {UserDtoMapper.class,
    BinaryContentDtoMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageDtoMapper {

  @Mapping(source = "channel.id", target = "channelId")
  @Mapping(source = "author", target = "author")
  @Mapping(source = "attachments", target = "attachments")
  MessageDto toDto(Message message);
}
