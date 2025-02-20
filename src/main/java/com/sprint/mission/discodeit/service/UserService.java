package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UpdateUserDto;
import com.sprint.mission.discodeit.dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
  boolean createUser(UserDto userDto);

  Optional<UserDto> findUser(UserDto userDto);

  List<UserDto> findAllUsers();

  void updateUser(UpdateUserDto updateUserDto);

  void deleteUser(UUID id);

  List<UserDto> getAllUsers();
}

