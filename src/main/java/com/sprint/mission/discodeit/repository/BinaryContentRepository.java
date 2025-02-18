package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.Map;
import java.util.UUID;

public interface BinaryContentRepository {
    Long save(BinaryContent binaryContent);

    BinaryContent load(Long id);

    Map<Long, BinaryContent> loadAll();

    void delete(Long id);

    // Content 존재 여부 확인
    boolean isBinaryContentExist(UUID referenceId);

    Map.Entry<Long, BinaryContent> findBinaryContentByUUID(UUID uuid);

    Map.Entry<Long, BinaryContent> findProfileImageByMessageId(UUID uuid);

    Map<Long, BinaryContent> findMessageImageByMessageId(UUID uuid);

    void deleteAllFileByReferenceId(UUID referenceId);
}
