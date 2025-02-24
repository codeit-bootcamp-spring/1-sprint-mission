package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusDTO.idResponse create(ReadStatusDTO.request readStatusReqDto);

    ReadStatusDTO.response find(Long id);

    ReadStatusDTO.response find(UUID uuid);

    List<ReadStatusDTO.response> findAllByUserId(UUID userId);

    ReadStatusDTO.idResponse update(ReadStatusDTO.update updateDto);

    ReadStatusDTO.idResponse delete(Long id);

    ReadStatusDTO.idResponse delete(UUID uuid);
}
