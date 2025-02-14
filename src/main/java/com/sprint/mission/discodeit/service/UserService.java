package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;

import java.util.List;

public interface UserService {
  UserResponseDto createUser(CreateUserRequest user);

  UserResponseDto findUserById(String id);

  List<UserResponseDto> findAllUsers();

  void updateUser(String id, UserUpdateDto updatedUser, String originalPassword);

  void deleteUser(String id, String password);

}
