package com.sprint.mission.discodeit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "channelId", source = "channel.id")
	ReadStatusDto toDto(ReadStatus readStatus);
}
