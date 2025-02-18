package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {


    UUID create(ReadStatusCreateDTO readStatusCreateDTO);
    ReadStatus find(UUID id);
    List<ReadStatus> findAll();
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(UUID id, ReadStatusUpdateDTO readStatusUpdateDTO);
    UUID delete(UUID id);
}
