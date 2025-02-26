package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;

import java.util.List;

public interface UserFindFacade {
  UserResponseDto findUserById(String id);
  List<UserResponseDto> findAllUsers();
}
