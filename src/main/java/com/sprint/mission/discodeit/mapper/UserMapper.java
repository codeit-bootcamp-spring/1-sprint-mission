package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserDto toDto(User user, boolean isOnline) {
    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        binaryContentMapper.toDto(user.getProfile()),
        isOnline,
        user.getRole()
    );
  }

  public UserDto toDto(User user) {
    return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            binaryContentMapper.toDto(user.getProfile()),
            true,
            user.getRole()
    );
  }
}
