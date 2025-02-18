package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.Map;
import java.util.UUID;

public interface BinaryContentRepository {
    // 저장
    void save(BinaryContent binaryContent);

    // 불러오기
    Map<UUID, BinaryContent> load();

    // 삭제
    void delete(UUID id);
}
