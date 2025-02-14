package com.sprint.mission.discodeit.service.facade;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;

import java.io.IOException;
import java.util.List;

public interface UserFacade {
  UserResponseDto createUser(CreateUserRequest request);
  UserResponseDto updateUser(String userId, UserUpdateDto updateDto, String plainPassword);
  UserResponseDto findUserById(String id);

  List<UserResponseDto> findAllUsers();
}
