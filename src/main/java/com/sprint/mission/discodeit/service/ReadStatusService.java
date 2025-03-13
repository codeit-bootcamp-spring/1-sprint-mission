package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  @Transactional
  ReadStatusDto createReadStatus(ReadStatusCreateRequest readStatusRequest);

  ReadStatusDto findReadStatusById(UUID readStatusId);

  // 프론트로 보내는 read-only 용이랑 백에서 데이터를 다루기 위한 엔티티 반환 메서드 나누는 건 어떤지...
  List<ReadStatusDto> findAllByUserId(UUID userId);

  List<ReadStatus> findAllReadStatusEntitiesByUserId(UUID userId);


  List<ReadStatusDto> findAllByChannelId(UUID channelId);


  @Transactional
  ReadStatusDto updateReadStatus(UUID id,
      ReadStatusUpdateRequest readStatusUpdateRequest);

  @Transactional
  void deleteReadStatusById(UUID readStatusId);

}
