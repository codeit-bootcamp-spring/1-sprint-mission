package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentResponse;

import java.util.Map;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentResponse create(BinaryContentRequest request);

    default BinaryContentResponse update(BinaryContentRequest binaryContent) {
        return null;
    }

    BinaryContentResponse find(UUID id);

    Map<UUID, BinaryContentResponse> findAllById(UUID id);

    default Map<UUID, BinaryContentResponse> findByUserId(UUID userId) {
        return null;
    }

    void delete(UUID userId);
}
