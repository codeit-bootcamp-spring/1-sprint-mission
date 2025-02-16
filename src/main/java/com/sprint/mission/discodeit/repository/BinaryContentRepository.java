package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    // 저장
    boolean save(BinaryContent binaryContent);

    // 조회
    Optional<BinaryContent> findById(UUID id);

    // 삭제
    boolean deleteById(UUID id);
}
