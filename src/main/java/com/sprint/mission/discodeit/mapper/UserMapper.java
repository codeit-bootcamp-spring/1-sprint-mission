package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserStatusMapper.class})
public interface UserMapper {

  @Mapping(target = "online", expression = "java(user.getStatus().isOnline())")
  UserDto toDto(User user);
}
