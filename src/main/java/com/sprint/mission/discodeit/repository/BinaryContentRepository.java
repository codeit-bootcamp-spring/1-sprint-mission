package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;

import java.util.HashMap;
import java.util.UUID;

public interface BinaryContentRepository {
    HashMap<UUID, BinaryContent> getBinaryContent(BinaryContentType type, UUID binaryContentId);
    HashMap<UUID, BinaryContent> getBinaryContentsMap(BinaryContentType type);
    boolean deleteBinaryContent(BinaryContentType type, UUID binaryContentId);
    boolean saveBinaryContent(BinaryContentType type, UUID binaryContentId);
    boolean isBinaryContentExist(BinaryContentType type, UUID binaryContentId);
}
