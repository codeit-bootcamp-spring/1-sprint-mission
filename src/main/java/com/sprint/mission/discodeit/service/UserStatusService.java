package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatusDto.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusDto;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  // 접속 상태 생성
  UserStatusDto create(CreateUserStatusRequest request);

  // 접속 상태 단일 검색
  UserStatusDto find(UUID userStatusId);

  // 접속 상태 다건 검색
  List<UserStatusDto> findAll();

  // 접속 상태 수정
  UserStatusDto update(UUID userStatusId, UpdateUserStatusRequest request);

  // 유저 id를 이용해 접속 상태 수정
  UserStatusDto updateByUserId(UUID userId, UpdateUserStatusRequest request);

  // 접속 상태 삭제
  void delete(UUID userStatusId);
}
