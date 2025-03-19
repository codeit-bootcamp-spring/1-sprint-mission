package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDto create(ReadStatusCreateRequest readStatusCreateRequest);

  ReadStatusDto find(UUID readStatusId);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest);

  void delete(UUID id);


}
