package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

  @Mapping(target = "userId", expression = "java(readStatus.getUser().getId())")
  @Mapping(target = "channelId", expression = "java(readStatus.getChannel().getId())")
  ReadStatusDto toReadStatusDto(ReadStatus readStatus);
}
