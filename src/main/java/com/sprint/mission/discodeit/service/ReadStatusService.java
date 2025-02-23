package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.entity.ReadStatus;
import com.sprint.mission.discodeit.web.dto.ReadStatusUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatus readStatus);

    Optional<ReadStatus> find(UUID readStatusId);

    List<ReadStatus> findAllByUserId(UUID userId);

    ReadStatus update(UUID readStatusId, ReadStatusUpdateDto readStatusUpdate);
    void delete(UUID readStatusId);
}
