package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
  User createUser(CreateUserDto user);

  UserResponseDto findUserById(String id);

  List<UserResponseDto> findAllUsers();

  void updateUser(String id, UserUpdateDto updatedUser, String originalPassword);

  void deleteUser(String id, String password);

}
