package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;

public class UserMapper {
  public static UserDto toDto(User user) {
    if(user == null) return null;
    return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getProfile(), user.getUserStatus().isOnline());
  }
}
