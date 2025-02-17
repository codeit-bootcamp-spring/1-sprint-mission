package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent createBinaryContent(String filePath, BinaryContentType contentType, UUID userId);
    BinaryContent createBinaryContent(String filePath, BinaryContentType contentType, UUID userId, UUID channelId);
    BinaryContent findAttachedContentById(UUID channelId, UUID binaryContentId);
    BinaryContent findProfilePictureById(UUID userId);
    List<BinaryContentDto> findAllBinaryContentsByChannelId(UUID channelId);
    Boolean deleteAttachedContentById(UUID binaryContentId, UUID channelId);
    Boolean deleteProfilePictureById(UUID binaryContentId, UUID userId);
}
