package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(BinaryContent content);
    BinaryContent find(UUID binaryContentId);
    List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);
    void delete(UUID binaryContentId);
}
