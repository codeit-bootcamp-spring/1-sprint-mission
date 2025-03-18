package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;
  private final UserStatusRepository userStatusRepository;

  public UserDto toDto(User user) {
    Boolean online = userStatusRepository.findById(user.getId())
        .map(userStatus -> userStatus.isOnline())
        .orElse(null);

    BinaryContentDto profile = binaryContentMapper.toDto(user.getProfile());

    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        profile,
        online
    );
  }
}
