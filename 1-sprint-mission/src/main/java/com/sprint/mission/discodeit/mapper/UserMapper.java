package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;

public class UserMapper {

  public UserDto toDto(User entity) {
    if (entity == null) {
      return null;
    }
    return new UserDto(
        entity.getId(),
        entity.getCreatedAt(),
        entity.getUpdatedAt(),
        entity.getUsername(),
        entity.getEmail(),
        entity.getProfile() != null
            ? entity.getProfile().getId()
            : null,
        entity.getStatus() != null && entity.getStatus().isOnline()
    );
  }

}
