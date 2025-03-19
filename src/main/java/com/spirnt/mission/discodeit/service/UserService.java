package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserService {

  // Create
  UserDto create(UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest);  // 유저 생성

  // Read
  UserDto find(UUID userId);    // 유저 정보 단건 조회

  List<UserDto> findAll(); // 모든 유저 조회

  // Update
  UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest);

  // Delete
  void delete(UUID userId);
}
