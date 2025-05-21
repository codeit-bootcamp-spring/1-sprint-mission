package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  // 유저 생성
  UserDto create(UserCreateRequest userRequest,
      Optional<BinaryContentCreateRequest> profileRequest);

  // 유저 단건 검색
  UserDto find(UUID userId);

  // 유저 다건 검색
  List<UserDto> findAll();

  // 유저 수정
  UserDto update(UUID userId, UserUpdateRequest userRequest,
      Optional<BinaryContentCreateRequest> profileRequest);

  // 유저 삭제
  void delete(UUID userId);
}