package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateDTO;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDto create(ReadStatusCreateDTO readStatusCreateDTO);

  ReadStatusDto findbyId(UUID uuid);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  ReadStatusDto update(ReadStatusUpdateDTO readStatusUpdateDTO);

  void delete(UUID uuid);

  void deleteByChannelId(UUID id);
}
