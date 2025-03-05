package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  User createUser(UserCreateRequestDto request
      , Optional<BinaryContentCreateRequestDto> profileCreateRequest);

  UserDto getUserById(UUID id);

  List<UserDto> getAllUsers();

  List<User> findAllUsers();

  User updateUser(UUID userId, UserUpdateRequestDto request
      , Optional<BinaryContentCreateRequestDto> profileCreateRequest);

  void deleteUser(UUID id);
}

