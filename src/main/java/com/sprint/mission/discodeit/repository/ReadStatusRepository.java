package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus createReadStatus(UUID id, ReadStatus readStatus);
    Optional<ReadStatus> findByUserId(UUID userId);
    List<ReadStatus> findAll();
}
