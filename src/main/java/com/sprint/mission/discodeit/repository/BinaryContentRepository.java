package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    BinaryContent find(UUID binaryContentId);
    List<BinaryContent> findAll();
    void delete(UUID binaryContentId);
    boolean existsById(UUID binaryContentId);
}
