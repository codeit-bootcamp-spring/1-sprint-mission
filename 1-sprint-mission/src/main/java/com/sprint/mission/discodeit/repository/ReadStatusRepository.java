package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.Map;
import java.util.UUID;

public interface ReadStatusRepository {
    Map<UUID, ReadStatus> loadReadStatusText();
    void saveReadStatusText();
}
