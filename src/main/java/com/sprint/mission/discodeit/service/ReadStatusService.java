package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    Long create(ReadStatusDTO.request readStatusReqDto);

    ReadStatusDTO.response find(Long id);

    ReadStatusDTO.response find(UUID uuid);

    List<ReadStatusDTO.response> findAllByUserId(UUID userId);

    void update(ReadStatusDTO.update updateDto);

    Long delete(Long id);

    Long delete(UUID uuid);
}
