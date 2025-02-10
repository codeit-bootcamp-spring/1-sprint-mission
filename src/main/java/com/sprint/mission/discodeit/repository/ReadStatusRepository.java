package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReadStatusRepository {

    ReadStatus save(ReadStatus readStatus);
    ReadStatus findById(UUID id);
    Map<UUID, ReadStatus> load();
    void delete(UUID id);

    List<ReadStatus> findAllByUserId(UUID userId); //한 id의 모든 readStatus반환
}
