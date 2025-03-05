package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CommonDTO;
import com.sprint.mission.discodeit.dto.ReadStatusDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    CommonDTO.idResponse create(ReadStatusDTO.request readStatusReqDto);

    ReadStatusDTO.response find(Long id);

    ReadStatusDTO.response find(UUID uuid);

    List<ReadStatusDTO.response> findAllByUserId(UUID userId);

    CommonDTO.idResponse update(Long readStatusId, Instant lastReadAt);

    CommonDTO.idResponse delete(Long id);

    CommonDTO.idResponse delete(UUID uuid);
}
