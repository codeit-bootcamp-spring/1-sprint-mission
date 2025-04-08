package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {


  ReadStatusDto create(ReadStatusCreateDTO readStatusCreateDTO);

  ReadStatusDto find(UUID id);

  List<ReadStatusDto> findAll();

  List<ReadStatusDto> findAllByUserId(UUID userId);

  ReadStatusDto update(UUID id, ReadStatusUpdateDTO readStatusUpdateDTO);

  void delete(UUID id);
}
