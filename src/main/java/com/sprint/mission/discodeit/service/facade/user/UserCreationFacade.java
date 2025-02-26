package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;

public interface UserCreationFacade {
  UserResponseDto createUser(CreateUserRequest userDto);
}
