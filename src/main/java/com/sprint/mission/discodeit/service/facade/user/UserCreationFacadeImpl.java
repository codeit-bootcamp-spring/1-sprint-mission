package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreationFacadeImpl implements UserCreationFacade {

  private final UserService userService;
  private final UserMapper userMapper;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  public UserResponseDto createUser(CreateUserRequest userDto) {

    User user = userMapper.toEntity(userDto, binaryContentMapper);

    user.updateStatus(new UserStatus(user.getUUID(), Instant.now()));
    userService.saveUser(user);

    userStatusService.create(user.getStatus());
    if (userDto.profileImage() != null && !userDto.profileImage().isEmpty()) {
      binaryContentService.create(user.getProfileImage());
    }

    return userMapper.toDto(user, user.getStatus(), user.getProfileImage());
  }
}
