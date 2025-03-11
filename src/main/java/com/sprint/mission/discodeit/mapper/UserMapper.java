package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentRepository binaryContentRepository;

  public UserDto toDto(User entity) {
    if (entity == null) {
      return null;
    }

    UserDto dto = new UserDto();
    dto.setId(entity.getId());
    dto.setUsername(entity.getUsername());
    dto.setEmail(entity.getEmail());
    dto.setProfile(binaryContentMapper.toDto(entity.getProfile()));
    dto.setOnline(entity.getStatus().isOnline());
    return dto;
  }
}
