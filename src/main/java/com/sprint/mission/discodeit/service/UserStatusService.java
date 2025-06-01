package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusDto;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  // 접속 상태 생성
  UserStatusDto create(UserStatusCreateRequest request);

  // 접속 상태 단일 검색
  UserStatusDto find(UUID userStatusId);

  // 접속 상태 다건 검색
  List<UserStatusDto> findAll();

  // 접속 상태 수정
  UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request);

  // 유저 id를 이용해 접속 상태 수정
  UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request);

  // 접속 상태 삭제
  void delete(UUID userStatusId);
}
