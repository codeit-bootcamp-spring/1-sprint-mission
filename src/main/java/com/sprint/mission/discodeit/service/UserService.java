package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;

import com.sprint.mission.discodeit.entity.User;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface UserService {

  User createUser(UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest);

  // Read : 전체 유저 조회, 특정 유저 조회
  List<UserFindResponse> showAllUsers();

  UserFindResponse getUserById(UUID id);

  // Update
  User updateUserInfo(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest);

  // Delete : 특정 유저 삭제
  void removeUserById(UUID id);
}
