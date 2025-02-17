package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent createBinaryContent(String profilePicturePath, BinaryContentType contentType, UUID userId);
    BinaryContent findBinaryContentById(UUID binaryContentId);
    List<BinaryContent> findAllBinaryContentsByIdIn(UUID channelId);
    Boolean deleteBinaryContentById(UUID binaryContentId);
}
