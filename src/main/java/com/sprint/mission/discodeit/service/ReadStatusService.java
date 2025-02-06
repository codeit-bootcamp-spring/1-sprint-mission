package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequest request);
    ReadStatusDto findById(UUID readStatusId);
    List<ReadStatusDto> findAllByUserId(UUID userId);
    ReadStatus update(UUID readStatusId, ReadStatusCreateRequest request);
    void delete(UUID readStatusId);
}
