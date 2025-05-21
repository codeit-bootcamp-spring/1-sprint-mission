package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  // 읽음 상태 생성
  ReadStatusDto create(ReadStatusCreateRequest request);

  // 읽음 상태 단건 검색
  ReadStatusDto find(UUID readStatusId);

  // 유저 id를 이용하 읽음 상태 다건 검색
  List<ReadStatusDto> findAllByUserId(UUID userId);

  // 읽음 상태 수정
  ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request);

  // 읽음 상태 삭제
  void delete(UUID readStatusId);
}
