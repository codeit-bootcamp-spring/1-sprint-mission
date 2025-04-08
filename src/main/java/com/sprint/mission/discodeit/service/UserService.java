package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;

import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  @Transactional
    // Create
  UserDto createUser(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

  // Read : 전체 유저 조회, 특정 유저 조회
  List<UserDto> showAllUsers();

  UserDto getUserById(UUID id);

  @Transactional
    // Update
  UserDto updateUserInfo(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

  @Transactional
    // Delete : 특정 유저 삭제
  void removeUserById(UUID id);
}
