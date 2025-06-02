package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.OnlineUserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public abstract class UserMapper {

  @Autowired
  protected OnlineUserService onlineUserService;

  // 기존 메서드는 호환성을 위해 유지
  @Mapping(target = "online", source = "online")
  public abstract UserDto toDto(User user, boolean online);

  @Mapping(target = "online", expression = "java(onlineUserService.isUserOnline(user))")
  public abstract UserDto toDto(User user);
}