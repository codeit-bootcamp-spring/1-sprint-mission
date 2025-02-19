package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import com.spirnt.mission.discodeit.enity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(BinaryContentCreate binaryContentCreate);

    BinaryContent find(UUID id);

    List<BinaryContent> findAllByIdIn(List<UUID> uuids);

    void delete(UUID id);

    BinaryContent findUserProfile(UUID userId);

    List<BinaryContent> findByMessageId(UUID messageId);

    void deleteUserProfile(UUID userId);

    void deleteByMessageId(UUID messageId);
}
