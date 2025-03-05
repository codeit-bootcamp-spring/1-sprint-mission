package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(UUID contentId, BinaryContentCreateRequest request);

    BinaryContent find(UUID contentId);

    List<BinaryContent> findAllByIdIn(List<UUID> contentIds);

    void delete(UUID contentId);
}
