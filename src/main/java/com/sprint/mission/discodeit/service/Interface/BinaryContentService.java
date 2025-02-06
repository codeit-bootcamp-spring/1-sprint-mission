package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent creat(BinaryContentCreateRequest request);
    Optional<BinaryContent> find(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
    void delete(UUID id);
    void deleteByUserId(UUID id);
}
