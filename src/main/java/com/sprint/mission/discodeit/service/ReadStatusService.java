package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusDto create(ReadStatusCreateRequest request);

    ReadStatusDto find(UUID readStatusId);

    List<ReadStatusDto> findAllByUserId(UUID userId);

    ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request);

    void delete(UUID readStatusId);
}
