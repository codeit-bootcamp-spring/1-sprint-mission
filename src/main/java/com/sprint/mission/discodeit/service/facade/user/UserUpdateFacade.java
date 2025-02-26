package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;

public interface UserUpdateFacade {
  UserResponseDto updateUser(String userId, UserUpdateDto updateDto);
}
