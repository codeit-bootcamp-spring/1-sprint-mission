package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserMapper(BinaryContentMapper binaryContentMapper) {
    this.binaryContentMapper = binaryContentMapper;
  }

  public UserDto toDto(User user) {
    if (user == null) {
      return null;
    }

    boolean isOnline = false;
    if (user.getStatus() != null) {
      isOnline = user.getStatus().isOnline();
    }

    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        binaryContentMapper.toDto(user.getProfile()),
        isOnline
    );
  }

  public List<UserDto> toDtoList(List<User> users) {
    if (users == null) {
      return Collections.emptyList();
    }

    return users.stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }
}