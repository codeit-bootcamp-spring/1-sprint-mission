package com.sprint.mission.discodeit.user.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sprint.mission.discodeit.user.dto.response.UserResponse;
import com.sprint.mission.discodeit.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "online", ignore = true)
		// User에는 없으므로 무시
	UserResponse toDto(User user);

	List<UserResponse> toDtoList(List<User> users);
}
