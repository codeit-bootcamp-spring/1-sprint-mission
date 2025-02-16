package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;

import java.util.List;

public interface UserMasterFacade {
  UserResponseDto createUser(CreateUserRequest request);
  UserResponseDto updateUser(String userId, UserUpdateDto updateDto);
  UserResponseDto findUserById(String id);
  List<UserResponseDto> findAllUsers();
  void deleteUser(String id, String password);
}
