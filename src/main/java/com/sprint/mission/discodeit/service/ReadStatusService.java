package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusDTO.CreateReadStatusDTO createReadStatusDTO);
    ReadStatus find(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(UUID id, ReadStatusDTO.UpdateReadStatusDTO updateReadStatusDTO);
    void delete(UUID id);
}
