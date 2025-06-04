package com.sprint.mission.discodeit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public interface UserMapper {

	@Mapping(target = "online", ignore = true)
	UserDto toDto(User user);

	@Mapping(target = "online", expression = "java(online)")
	UserDto toDto(User user, boolean online);
}
