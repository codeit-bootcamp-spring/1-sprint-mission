package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(BinaryContentCreateRequest request);
    BinaryContent findByContentId(UUID contentId);
    BinaryContent findByUserId(UUID userId);

    List<BinaryContent> findAllByIdIn(List<UUID> ids);

    void deleteByContentId(UUID id);

    void deleteByUserId(UUID userId);
}
