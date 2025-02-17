package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.UUID;

public interface BinaryContentRepository {
    LinkedHashMap<UUID, BinaryContent> getBinaryContentsMap(BinaryContentType type, UUID channelId);
    BinaryContent getBinaryContent(BinaryContentType type, UUID channelId, UUID binaryContentId);
    boolean deleteBinaryContentMap(BinaryContentType type, UUID channelId);
    boolean deleteBinaryContent(BinaryContentType type, UUID channelId, UUID binaryContentId);
    boolean saveBinaryContentsMap(BinaryContentType type, UUID channelId, LinkedHashMap<UUID, BinaryContent> binaryContentMap);
    boolean addBinaryContent(BinaryContentType type, UUID channelId, BinaryContent binaryContent);
    boolean isBinaryContentExist(BinaryContentType type, UUID channelId, UUID binaryContentId);
}