package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentService {
    void createProfile(BinaryContentCreateRequest request);
    void createMessage(BinaryContentCreateRequest request);
    Optional<BinaryContent> find(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
    void delete(UUID id);
    void deleteByMessageId(UUID id);
    void deleteByUserId(UUID id);
}
