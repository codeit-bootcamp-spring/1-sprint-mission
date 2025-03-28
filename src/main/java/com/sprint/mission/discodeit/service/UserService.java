package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  // 유저 생성
  UserDto create(CreateUserRequest userRequest,
      Optional<CreateBinaryContentRequest> profileRequest);

  // 유저 단건 검색
  UserDto find(UUID userId);

  // 유저 다건 검색
  List<UserDto> findAll();

  // 유저 수정
  UserDto update(UUID userId, UpdateUserRequest userRequest,
      Optional<CreateBinaryContentRequest> profileRequest);

  // 유저 삭제
  void delete(UUID userId);
}