package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus saveReadStatus(ReadStatus readStatus);
    ReadStatus findReadStatusById(UUID readStatusId);
    Optional<ReadStatus> findReadStatusByUserId(UUID userId);
    void removeReadStatus(UUID readStatusId);
}
