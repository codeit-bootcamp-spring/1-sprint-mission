package com.sprint.mission.discodeit.mapper.entitymapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;

public class UserMapper {

  public static UserDto toDto(User user) {
    if (user == null) {
      throw new UserNotFoundException();
    }

    BinaryContentDto binaryContentDto = BinaryContentMapper.toDto(user.getProfile());

    return new UserDto(user.getId(), user.getUsername(), user.getEmail(), binaryContentDto, true);
  }
}
