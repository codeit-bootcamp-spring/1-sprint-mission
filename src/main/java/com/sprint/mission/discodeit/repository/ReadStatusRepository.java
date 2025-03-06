package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.Map;
import java.util.UUID;

public interface ReadStatusRepository {
    // 저장
    void save(ReadStatus readStatus);

    // 불러오기
    Map<UUID, ReadStatus> load();

    // 삭제
    void delete(UUID id);
}
