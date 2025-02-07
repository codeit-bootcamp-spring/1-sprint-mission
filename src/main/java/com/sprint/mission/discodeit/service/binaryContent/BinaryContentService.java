package com.sprint.mission.discodeit.service.binaryContent;

import com.sprint.mission.discodeit.dto.binaryContentService.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(BinaryContentCreateRequestDTO request);
    BinaryContent find(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> id);
    void delete(UUID id);
}
