package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(BinaryContentDTO binaryContentDTO);
    BinaryContent findById(UUID id);
    List<BinaryContent> findAllById(List<UUID> uuids);
    BinaryContent findUserProfile(UUID userId);
    List<BinaryContent> findByMessageId(UUID messageId);
    void delete(UUID id);
    void deleteUserProfile(UUID userId);
}
