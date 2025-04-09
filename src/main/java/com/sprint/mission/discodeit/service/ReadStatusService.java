package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDto create(ReadStatusRequest readStatusRequest);

  ReadStatusDto findbyId(UUID uuid);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  ReadStatusDto update(ReadStatusUpdateRequest readStatusUpdateRequest);

  void delete(UUID uuid);

  void deleteByChannelId(UUID id);
}
