package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent saveBinaryContent(BinaryContent binaryContent);
    BinaryContent findBinaryContentById(UUID binaryContentId);
    List<BinaryContent> findAllBinaryContentById(List<UUID> binaryContentIdList);
    void removeBinaryContent(UUID binaryContentId);
}
