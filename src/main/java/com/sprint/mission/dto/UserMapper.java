package com.sprint.mission.dto;

import com.sprint.mission.dto.response.UserDto;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.BinaryContent;
import com.sprint.mission.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "online", expression = "java(user.getStatus() != null ? user.getStatus().isOnline() : null)")
  UserDto toDto(User user);

  User toEntityWithoutProfile(UserDtoForCreate userDto);

  User toEntityWithProfile(UserDtoForCreate userDto, BinaryContent profile);
}
