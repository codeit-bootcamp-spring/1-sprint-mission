package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.enity.ReadStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "channel.id", target = "channelId")
  ReadStatusDto toDto(ReadStatus readStatus);

}
