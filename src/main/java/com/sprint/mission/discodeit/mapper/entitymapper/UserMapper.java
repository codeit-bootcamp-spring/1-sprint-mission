package com.sprint.mission.discodeit.mapper.entitymapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import java.time.Instant;
import java.util.Map;

public class UserMapper {

  public static UserDto toDto(User user) {
    if (user == null) {
      throw new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
          Map.of(ErrorCode.USER_NOT_FOUND.getCode(), ErrorCode.USER_NOT_FOUND.getMessage())
      );
    }

    BinaryContentDto binaryContentDto = BinaryContentMapper.toDto(user.getProfile());

    return new UserDto(user.getId(), user.getUsername(), user.getEmail(), binaryContentDto, true);
  }
}
