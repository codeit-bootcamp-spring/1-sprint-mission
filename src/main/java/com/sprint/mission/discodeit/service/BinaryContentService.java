package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(CreateBinaryContentRequest request);

    Optional<BinaryContent> find(UUID id);

    List<BinaryContent> findAllByIdIn(List<UUID> uuidList);

    void delete(UUID id);
}
